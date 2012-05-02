#!/usr/bin/perl

open FILE, "<$ARGV[0]" or die $!;
undef $/;
$msg=<FILE>;
close(FILE);

if ( $msg =~ /
	(^KOKU-\d{1,5}:\ [\w\ ]{10,60}$(\n\n\w.*)?(^\#[^\r\n]*$)*)	# normal commit comment
	|(^Merge\ branch\ '[^']+'$.*)								# branch merge comment
	/mx ) {
  exit(0);
}

exit(1);
