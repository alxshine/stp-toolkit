from __future__ import division
from __future__ import division
from __future__ import division
from __future__ import division
from __future__ import division
from __future__ import division
from __future__ import division
from __future__ import division
from __future__ import division
import fileinput
import sys

if len(sys.argv) < 3:
    print ("usage: " + str(sys.argv[0]) + " inputFile outputFile")
    sys.exit(1)

f = open(sys.argv[2], 'w')

for line in fileinput.input(sys.argv[1]):
    parts = line.rstrip().split("\t")
    try:
        newline = str(float(parts[0])/10) + "\t" + parts[1] + "\n"
        f.write(newline)
    except ValueError:
        f.write(line)

f.close()
