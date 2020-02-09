# Triangle Service Known Issues

###1. Documentation issues:
#####a.input formant should be defined:
    type - Non-integers are acceptable, it seems that double formate is used 
    boundary values
    Exponential format - 2.99792458e8 
    max/min value
#####b.valid separator? 
    Does moltiple separater (like ";:") supported?
#####c.valid triangle?
    collapsed case 
        1;1;2
        3;3;3
        0;0;0 
        
###2. POST /triangle issues:       
    "input": "3;4;-5" - create request with negative numbers on any position is acceptable.
    expected: code <422> actual: code <200>
    "input": "-3;4;5" 
    expected: code <422> actual: code <200>
    "input": "3;-4;5"
    expected: code <422> actual: code <200>
    "input": "3;4;5;6" - Create request with more than 3 numbers is acceptable. 
    expected: code <200> actual: code <422>

##3 separator isses: "."
    not clear how to use dot as separator and how to escape it


##4 capacity isses: possible to add 11 triangles
     testcase CapacityTest.maxCapacityAndAbove

##5 authorization issue: AuthorizationTest
#####authorization fail if header starts with space 
    X-User= 38e1e2f8-5428-4833-a38b-054fb6522a95
     X-User=38e1e2f8-5428-4833-a38b-054fb6522a95
    Expected status code <401> but was <500>.
    