import random
import csv
def getRandom():
    return int(random.random()*1000)

def generateData():
    output = []
    output.append(['x1','x2','x3','x4','x5','x6','y'])
    for i in range(0, 50000):
        x1=getRandom()
        x2=getRandom()
        x3=getRandom()
        x4=getRandom()
        x5=getRandom()
        x6=getRandom()        
        y=x1+x2+x3+x4+x5-x6
        output.append([x1,x2,x3,x4,x5,x6,y])
        
    with open('generated_dataset.csv', 'w', newline='') as fp:
        a = csv.writer(fp, delimiter=',')
        a.writerows(output)
        
generateData()  
