#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <netinet/in.h>
#include <pcap.h>
#include <string.h>
/*
#include <ctype.h>
#include <errno.h>
#include <sys/socket.h>
*/
#include <arpa/inet.h>

#define SIZE_ETHERNET 14
#define ETHER_ADDR_LEN	6

#define DEBUG_MODE 1

/*
libpcapのサンプル
ubuntuとかdebianのひとは
libpcap0.8-dev をapt-getとかで入れる
コンパイルするときはライブラリでpcapを指定する
gcc for_cpsf.c -lpcap
詳しくはここにかいてあるので宛先が80番ポートのTCPパケットだけ表示するプログラムを書いてください．
*/

//イーサネットヘッダ
struct struct_ethernet {
  u_char  ether_dhost[ETHER_ADDR_LEN];
  u_char  ether_shost[ETHER_ADDR_LEN];
  u_short ether_type;
};

// ip header
struct struct_ip {
  u_char ip_vhl;
  u_char ip_tos;
  u_short ip_len;
  u_short ip_id;
  u_short ip_off;
  #define IP_RF 0x8000
  #define IP_DF 0x4000
  #define IP_MF 0x2000
  #define IP_OFFMASK 0x1fff
  u_char ip_ttl;
  u_char ip_p;
  u_short ip_sum;
  struct in_addr ip_src, ip_dst;
};
#define IP_HL(ip) (((ip)->ip_vhl) & 0x0f)
#define IP_V(ip)  (((ip)->ip_vhl) >> 4)

// tcp headera
typedef u_int tcp_seq;
struct struct_tcp {
  u_short th_sport;
  u_short th_dport;
  tcp_seq th_seq;
  tcp_seq th_ack;
  u_char th_offx2;
  #define TH_OFF(th)  (((th)->th_offx2 & 0xf0) >> 4)
  u_char th_flags;
  #define TH_FIN 0x01
  #define TH_SYN 0x02
  #define TH_RST 0x04
  #define TH_PUSH 0x08
  #define TH_ACK 0x10
  #define TH_URG 0x20
  #define TH_ECE 0x40
  #define TH_CWR 0x80
  #define TH_FLAGS (TH_FIN|TH_SYN|TH_RST|TH_ACK|TH_URG|TH_ECE|TH_CWR)
  u_short th_win;
  u_short th_sum;
  u_short th_urp;
};

main(int argc, char *argv[]) {
  pcap_t *pd;
  int snaplen = 64;
  int pflag = 0;
  int timeout = 1000;
  char ebuf[PCAP_ERRBUF_SIZE];
  bpf_u_int32 localnet, netmask;
  pcap_handler callback;
  void print_ethaddr(u_char *, const struct pcap_pkthdr *, const u_char *packet);
  struct bpf_program;
  
  //macならen0とかubuntuならeth1とか
  if ((pd = pcap_open_live("en1", snaplen, !pflag, timeout, ebuf)) == NULL) {
    exit(1);
  }	
  
  if (pcap_lookupnet("en1", &localnet, &netmask, ebuf) < 0) {
    exit(1);
  }
  callback = print_ethaddr;
  if (pcap_loop(pd, -1, callback, NULL) < 0) {
    exit(1);
  }
  pcap_close(pd);
  exit(0);
}

void print_ethaddr(u_char *args, const struct pcap_pkthdr *header, const u_char *packet){
  const struct struct_ethernet *eh;        
  const struct struct_ip *ip;
  const struct struct_tcp *tcp;
  u_int size_ip;
  u_int size_tcp;
  eh = (struct struct_ethernet *)(packet);
  ip = (struct struct_ip *)(packet + SIZE_ETHERNET);
  size_ip = IP_HL(ip) * 4;
  tcp = (struct struct_tcp *)(packet + SIZE_ETHERNET + size_ip);
  int i;
  
  if(strcmp("192.168.100.101", inet_ntoa(ip->ip_src)) == 0 || strcmp("192.168.100.101", inet_ntoa(ip->ip_dst)) == 0){
//  if (DEBUG_MODE || tcp->th_sport == 80 || tcp->th_sport == 443 || tcp->th_dport == 80 || tcp->th_dport == 443) {

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
    printf("%d -> %d\n", tcp->th_sport, tcp->th_dport);

    // packet length
    printf("packet length:%d\n", ip->ip_len);

    printf("\n");
}
}
