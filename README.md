# FEUP-SDIS
Code developed in the SDIS class, in the MIEIC course, in FEUP

STEPS TO RUN:

1. Run makefile
1. Start rmiregistry in /bin/ folder (start rmiregistry for Windows, rmiregistry for Linux)
1. Start Peers with java Peer 1.0 1 <peer_ap> 224.0.0.0 4445 224.0.0.1 4446 224.0.0.2 4447 (example ports/ips, change at will)
1. Start TestApp with java TestApp <peer_ap> <sub_protocol> <opnd_1> <opnd_2>  

sub_protocol = BACKUP | RESTORE | RECLAIM | DELETE
opnd_1 = file_path for Backup, Restore and Delete, space for Reclaim
opnd_2 = rep_degree for Backup only
