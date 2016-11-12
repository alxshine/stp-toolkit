#include <stdio.h>
#include <stdlib.h>

#include <pcap/pcap.h>


int main(int argc, char **args){
    if(argc<3){
        fprintf(stderr, "usage: %s filename capture-length\n", args[0]);
        return -1;
    }

    char errbuf[1024];
    pcap_t *pcap = pcap_open_offline(args[1], errbuf);
    struct pcap_pkthdr *header;
    const u_char *data;

    if(pcap_next_ex(pcap, &header, &data) < 0){
        fprintf(stderr, "file is empty\n");
        return -1;
    }

    //we want to know how many seconds we need to store
    //we have hundredths of seconds as resolution
    int numSeconds = atoi(args[2])*10;
    unsigned int counts[numSeconds];
    for(int i=0; i<numSeconds; i++)
        counts[i] = 0;

    //we want it to be possible to submit a filter string
    if(argc>3){
        char *filterString = args[3];
        printf("s\t%s\n", args[3]);
        struct bpf_program fp;
        if(pcap_compile(pcap, &fp, filterString, 0, PCAP_NETMASK_UNKNOWN)<0){
            fprintf(stderr, "could not compile filter\n");
            return -1;
        }
        if(pcap_setfilter(pcap, &fp) < 0){
            fprintf(stderr, "could not set filter\n");
            return -1;
        }
    }

    long firststamp = header->ts.tv_sec;
    long firstmicros = header->ts.tv_usec;

    while(pcap_next_ex(pcap, &header, &data) >= 0){
        int seconds = header->ts.tv_sec - firststamp;
        long micros = header->ts.tv_usec - firstmicros;
        //if micros is <0, we need to decrease the seconds by one
        //and add 1 million, to make them positive
        if(micros<0){
            micros += 1000000;
            seconds--;
        }
        //to get to tenths, we need to divide by 10^5
        int tenths = micros/100000;
        counts[seconds*10+tenths]++;
    }


    for(int i=0; i<numSeconds; i++)
        printf("%d\t%d\n", i+1, counts[i]);
}
