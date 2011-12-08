#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <pcap.h>
#include <string.h>
#include <arpa/inet.h>

#define SIZE_ETHERNET 14
#define ETHER_ADDR_LEN	6

// print packet datas
void print_ethaddr(u_char *, const struct pcap_pkthdr *, const u_char *packet);

/*
libpcapのサンプル
ubuntuとかdebianのひとは
libpcap0.8-dev をapt-getとかで入れる
コンパイルするときはライブラリでpcapを指定する
gcc for_cpsf.c -lpcap
詳しくはここにかいてあるので宛先が80番ポートのTCPパケットだけ表示するプログラムを書いてください．
*/

/**
ヘッダーの詳細はここを参考にした
http://net-newbie.com/tcpip/packets.html
後の値はサイズとヘッダー内の合計サイズ
*/
//イーサネットヘッダ
struct struct_ethernet {
  u_char  ether_dhost[ETHER_ADDR_LEN];  /* 6, 宛先のMAC addr */
  u_char  ether_shost[ETHER_ADDR_LEN];  /* 6(12), 送信元のMAC addr */
  u_short ether_type; /* 2(14), フレムタイプ */
};

// ip header
struct struct_ip {
  u_char ip_vhl;  /* 1, パケットのバージョン(前4bit)とヘッダー長(後4bit) */
  u_char ip_tos;  /* 1(2), サービスのタイプ */
  u_short ip_len; /* 2(4), IPパケット全体の長さ */
  u_short ip_id;  /* 2(6), データグラムの識別子 */
  u_short ip_off; /* 2(8), フラグとフラグメントオフセット */
  #define IP_RF 0x8000
  #define IP_DF 0x4000
  #define IP_MF 0x2000
  #define IP_OFFMASK 0x1fff
  u_char ip_ttl;  /* 1(9), パケットの生存時間 */
  u_char ip_p;  /* 1(10), プロトコル */
  u_short ip_sum; /* 2(12), ipヘッダーのチェックサム */
  struct in_addr ip_src;  /* 4(16), 送信元のipアドレス */
  struct in_addr ip_dst;  /* 4(20), 宛先のipアドレス */
};
/* ip_vhlの前4bit分と後4bit分の値 */
#define IP_HL(ip) (((ip)->ip_vhl) & 0x0f) /* バージョン情報,後4bit分 */
#define IP_V(ip)  (((ip)->ip_vhl) >> 4) /* バケットlength，前4bit分 */

// tcp headera
typedef u_int tcp_seq;
struct struct_tcp {
  u_short th_sport; /* 2, 送信元のport */
  u_short th_dport; /* 2(4), 宛先のport */
  tcp_seq th_seq; /* 4(8), シーケンス番号 */
  tcp_seq th_ack; /* 4(12), ack番号 */
  u_char th_offx2;  /* 1(13), データオフセット */
  #define TH_OFF(th)  (((th)->th_offx2 & 0xf0) >> 4)
  u_char th_flags; /* 1(14), フラグ */
  #define TH_FIN 0x01
  #define TH_SYN 0x02
  #define TH_RST 0x04
  #define TH_PUSH 0x08
  #define TH_ACK 0x10
  #define TH_URG 0x20
  #define TH_ECE 0x40
  #define TH_CWR 0x80
  #define TH_FLAGS (TH_FIN|TH_SYN|TH_RST|TH_ACK|TH_URG|TH_ECE|TH_CWR)
  u_short th_win; /* 2(16), ウィンドウ */
  u_short th_sum; /* 2(18), チェックサム */
  u_short th_urp; /* 緊急ポインタ */
};

main(int argc, char *argv[]) {
  pcap_t *pd;
  int snaplen = 64;
  int pflag = 0;
  int timeout = 1000;
  char ebuf[PCAP_ERRBUF_SIZE];
  bpf_u_int32 localnet, netmask;
  pcap_handler callback;
  struct bpf_program bpf_p;

  // 引数がコマンドラインからifデバイス名を指定 */
  char *if_name;
  if(argc < 2) {
    strcpy(if_name, "en1");
  } else {
    strcpy(if_name, argv[1]);
  }

  if ((pd = pcap_open_live(if_name, snaplen, !pflag, timeout, ebuf)) == NULL) {
    exit(1);
  }	
  
  if (pcap_lookupnet(if_name, &localnet, &netmask, ebuf) < 0) {
    exit(1);
  }

  // port 80 のパケットフィルター
/*  if (pcap_compile(pd, &bpf_p, "port 80", 0, netmask) == -1) { 
    fprintf(stderr,"Error calling pcap_compile\n");
    exit(1); 
  }
  if (pcap_setfilter(pd, &bpf_p) == -1) { 
    fprintf(stderr,"Error setting filter\n");
    exit(1); 
  }
*/

  // callbackにprint_ethaddrを入れてぶん回す
  callback = print_ethaddr;
  if (pcap_loop(pd, -1, callback, NULL) < 0) {
    exit(1);
  }

  pcap_close(pd);
  exit(0);
}

void print_ethaddr(u_char *args, const struct pcap_pkthdr *header, const u_char *packet){
  
  // packetの中身をstructのいろいろに入れる
  const struct struct_ethernet *eh;        
  const struct struct_ip *ip;
  const struct struct_tcp *tcp;

  // ipとtcpのサイズ
  u_int size_ip;
  u_int size_tcp;

  // packetのethernetの部分を入れる
  eh = (struct struct_ethernet *)(packet);

  // packetのipの部分を入れるのとサイズ
  ip = (struct struct_ip *)(packet + SIZE_ETHERNET);
  size_ip = IP_HL(ip) * 4;

  // packetのtcpの部分
  tcp = (struct struct_tcp *)(packet + SIZE_ETHERNET + size_ip);
  
  // for用
  int i;

  // size_ip < 20 ならreturn;
  if(size_ip < 20) {
    return;
  }

  // mac addr
  printf("MAC address\n");
  //送信元MACアドレス
  for (i = 0; i < 6; ++i) {
    printf("%02x", (int)eh->ether_shost[i]);
    if(i < 5){
      printf(":");
    }
  }
  printf(" -> ");
  //送信先MACアドレス
  for (i = 0; i < 6; ++i) {
    printf("%02x", (int)eh->ether_dhost[i]);
    if(i < 5){
      printf(":");
    }
  }
  printf("\n");

  // ip 
  printf("IP address\n");
  printf("%s -> %s\n", inet_ntoa(ip->ip_src), inet_ntoa(ip->ip_dst));
  
  // port
  printf("PORT number\n");
  printf("%d -> %d\n", ntohs(tcp->th_sport), ntohs(tcp->th_dport));

  // packet length
  printf("packet length:%d\n", ip->ip_len);
  printf("\n");
  
}
