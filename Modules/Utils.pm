package Utils;

use strict;
use warnings;
use Exporter qw(import);

our @ISA = qw(Exporter);
our @EXPORT_OK = qw(validate_ipv6_address validate_ipv4_address);

# Validates whether the given string is a valid IPv6 address.
#
# Arguments:
#   $address - The string to validate.
#
# Returns:
#   1 if the string is a valid IPv6 address, 0 otherwise.
sub validate_ipv6_address {
    my ($address) = @_;

    # Check if the address is a valid IPv6 address
    if ($address =~ /^
        (
            # 8 groups of 4 hexadecimal digits, separated by colons
            ([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}
            |
            # 1 or more groups of 4 hexadecimal digits, separated by colons, followed by an IPv4 address
            ([0-9a-fA-F]{1,4}:){6}(0|([1-9a-fA-F][0-9a-fA-F]{0,3})):(\d{1,3}\.){3}\d{1,3}
            |
            # 1 or more groups of 4 hexadecimal digits, separated by colons, followed by a double colon and 1 or more groups of 4 hexadecimal digits, separated by colons
            ([0-9a-fA-F]{1,4}:){1,5}:([0-9a-fA-F]{1,4}:){1,5}[0-9a-fA-F]{1,4}
            |
            # 1 or more groups of 4 hexadecimal digits, separated by colons, followed by a double colon and an IPv4 address
            ([0-9a-fA-F]{1,4}:){1,5}:((\d{1,3}\.){3}\d{1,3}|[0-9a-fA-F]{1,4}:\d{1,3}\.\d{1,3}\.\d{1,3}\.\d{1,3})
        )
        $/x) {
        return 1;
    } else {
        return 0;
    }
}

sub validate_ipv4_address {
    my ($address) = @_;

    # Check if the address is a valid IPv4 address
    if ($address =~ /^
        (
            # 1-3 digits followed by a dot, repeated 3 times, followed by 1-3 digits
            (\d{1,3}\.){3}\d{1,3}
        )
        $/x) {
        return 1;
    } else {
        return 0;
    }
}

1;
