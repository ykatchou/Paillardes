#!/usr/bin/python

from objects import *
from database import *
from core import *

####
#### M A I N
######################################################

class Main:
    def main(self):
        db = DB()

        dbload = DatabaseLoader()
        dbload.loadingChansons(db)
        dbload.loadingFilliere(db)
        dbload.loadingTag(db)
        dbload.loadingChansonFilliere(db)
        dbload.loadingChansonTag(db)

        print'done.'

####
#### I N I T I A L   L A U N C H E R
######################################################
m = Main()
m.main()



