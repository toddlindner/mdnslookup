# mdnslookup

A small program that scans your multicast dns and outputs a hosts file

Internally it just executes dig:
* dig +short +tries=1 +timeout=1 -x 10.0.1.1 @224.0.0.251 -p 5353

Put something like this into your crontab
* src/main/sh/discover-hosts
