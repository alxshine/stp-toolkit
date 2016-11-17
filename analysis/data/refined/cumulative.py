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

s = 0
c = 0

f = open(sys.argv[2], 'w')

for line in fileinput.input(sys.argv[1]):
    parts = line.rstrip().split("\t")
    try:
        c += int(parts[1])
        newline = str(float(parts[0])/10) + "\t" + str(c) + "\n"
        f.write(newline)
    except ValueError:
        f.write(line)

f.close()
