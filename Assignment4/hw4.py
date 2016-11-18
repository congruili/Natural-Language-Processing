__author__ = 'congrui_li'

import string 
import sys

def main(argv):
    infile = open(argv[1], 'r')
    outfile = open(argv[2], 'w+')

    i = 0
    odd = True

    for line in infile:
        i = i + 1
        if (i < 2):
            outfile.write(line)
            continue
        else:
            if odd:
                outfile.write(line)
            else:
                line = line.strip("\n").lower()
                line = line[::-1]
                rot13 = line.encode('rot13')
                outfile.write(rot13 + "\n")
            odd = not odd

    infile.close()
    outfile.close()

if __name__ == "__main__":
    main(sys.argv)
