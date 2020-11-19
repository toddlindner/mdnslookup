# mdnslookup

A small program that scans your multicast dns and outputs a hosts file.

This can be useful for generating a hosts file for pi-hole when conditional forwarding does not work on your router.

Internally it just executes dig:
* dig +short +tries=1 +timeout=1 -x 10.0.1.1 @224.0.0.251 -p 5353

Put something like this into your crontab
* java MDNSLookup.java 200 > hosts.discovered

Then create a file /etc/dnsmasq.d/02-myhosts.conf
* addn-hosts=hosts.discovered
