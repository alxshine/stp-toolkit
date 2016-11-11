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
    int numSeconds = atoi(args[2]);
    unsigned int counts[numSeconds];
    for(int i=0; i<numSeconds; i++)
        counts[i] = 0;

    //we want it to be possible to submit a filter string
    if(argc>3){
        char *filterString = args[3];
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

    while(pcap_next_ex(pcap, &header, &data) >= 0){
       int seconds = header->ts.tv_sec - firststamp;
       counts[seconds]++;
    }

    for(int i=0; i<numSeconds; i++)
        printf("%d\n", counts[i]);
}
