#include <stdio.h>
#include <stdlib.h>
#include <dirent.h>
#include <sys/types.h>
#include <sys/stat.h>

int main(int argc, char *argv[]){

	/** direcroty name */
	char *name;
	/** directory */
	DIR *dir;
	/** directory information */
	struct dirent *entry;
	/** state of directory */
	struct stat statbuf;

	/** 引数なしなら現在のdir */
	if(argc < 2){
		name = getenv("PWD");
	}else{
		name = argv[1];
	}

	/** open directory */
	if((dir = opendir(name)) == NULL){
		printf("error, no such directory\n");
		return 0;
	}

	/** read directory */
	while((entry = readdir(dir)) != NULL){
		/** entryに格納したファイルの情報をstatbufに格納 */
		stat(entry->d_name, &statbuf);
		/** ファイルの情報別に出力 */
		if(S_ISDIR(statbuf.st_mode)){
			/** ディレクトリなら文末に/ */
			printf("%s/\n", entry->d_name);
		}else if(S_ISREG(statbuf.st_mode)){
			printf("%s\n", entry->d_name);
		}
	}

	/** close directory */
	closedir(dir);

	return 0;

}
