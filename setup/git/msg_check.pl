#!/usr/bin/perl

open FILE, "<$ARGV[0]" or die $!;
undef $/;
$msg=<FILE>;
close(FILE);

if ( $msg =~ /^KOKU-\d{1,5}: [\w ]{10,50}$(\n\n\w.*)?(^#[^\r\n]*$)*/m ) {
  exit(0);
}

exit(1);
