#!/usr/bin/perl

open FILE, "<$ARGV[0]" or die $!;
undef $/;
$msg=<FILE>;
close(FILE);

# automatic commit messages e.g. merge commits of the form:
# Merge branch 'develop'
# bypass service hooks so they don't need to be explicitly allowed.
if ( $msg =~ /
	(^KOKU-\d{1,5}:\ [\w\ ]{10,60}$(\n\n\w.*)?(^\#[^\r\n]*$)*)	# normal commit comment
	/mx ) {
  exit(0);
}

exit(1);
