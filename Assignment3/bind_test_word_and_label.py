__author__ = 'congrui_li'

import string 
import sys

def main(argv):
    token_file = open(argv[1], 'r')
    label_file = open(argv[2], 'r')
    outfile = open(argv[3], 'w+')

    for token, label in zip(token_file, label_file):
        if token.strip() == "" and label.strip() == "":
            outfile.write("\n")
        else:
            curt = token.strip() + " " + label.strip() + "\n"
            outfile.write(curt)

    token_file.close()
    label_file.close()
    outfile.close()

if __name__ == "__main__":
    main(sys.argv)
