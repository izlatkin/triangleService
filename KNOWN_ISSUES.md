# Triangle Service Known Issues

###1. Documentation issues:
#####a.input formant should be defined:
    type
    boundary values
    Exponential format - 2.99792458e8 
    max/min value
    string values
#####b.valid separator? 
    Does moltiple separater (like ";:") supported?
#####c.valid triangle?
    collapsed case 
        1;1;2
        3;3;3
        0;0;0 
        
###1. POST /triangle issues:       
#####"input": "3;4;-5" 
expected: code <422> actual: code <200>
#####"input": "-3;4;5"
expected: code <422> actual: code <200>
#####"input": "3;-4;5"
expected: code <422> actual: code <200>
#####"input": "3;4;5;6" 
expected: code <200> actual: code <204220>

## separator isses: "."
#####not clear how to use dot as separator and how to ecranete it
    