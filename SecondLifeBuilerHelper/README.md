Second Life - Builder Helper
=======

# Description
Did you ever dream to easily create buildings in Second Life without needing to manually move walls, floors and stairs? Furthermore, if you don't own a land, keeping your big houses in many parts can become long and boring to always raise them.

Who needs this tool:
* Anyone who wants to create rapidly an house skeleton
* The professional builder who wants to offer a fast construction of the base to concentrate more on the design
* Anyone who wants to impress with animated constructions

What can be made:
* Houses with many floors
* Big garden (change the walls for trees)
* Some mazes and dungeons for those who like to play
* High buildings with repeated floors (Babel tour)
* Constructions that disappear by themselves

Limitations:
* When a script that is generated with that tool contains approximately more than 180 lines, Second Life risks to give you an error when the script will be saved.
  * That is why this tool is optimizing the generated script to make loops with redundant code
  * If this situation happens to you, you simply need to put your scene in many parts like one floor at a time
  * Seriously, it is rare that this happens for normal buildings. It is more with big maze that it happens.
* If there are too much moves made (llSetPos), during the execution of the script, Second Life will ignore those calls after a big amount has been processed
  * This is really rare too since this tool tends to minimize the moves
  
# Tutorial and Ideas

## Basics
* How to use the features of this software
* How to use the generated script in SecondLife

http://www.youtube.com/watch?v=ELHDRHq0iw0

## Babel Tour
With this tool, you can create all the floors you want and then simply says to replicate them 5 times or more. This will make a Babel Tour. The option is in the "Properties" dialog.

Here is an example of structure that repeat itself.

http://www.youtube.com/watch?v=G5CFnlV85To

## Maze
You know, it is easy to successfully find the exit on a paper maze since you can see it all from top. But, when placed inside, to be able to exit when every sides look the same is more of a challenge.

Here is an example of maze. You just need to add a roof to have the pleasure of living this adventure.

http://www.youtube.com/watch?v=PbTHEm8Ggdw

## Pyramid
Here is how to create a Pyramid.

http://www.youtube.com/watch?v=dnVJda3Jd68

## Constructions that disappear by themselves
The generated script has some hidden tricks. One of them is the live variable that appears at the beginning:

```
// floor - 2.0x2.0x0.1
// wall - 0.1x2.1x3.0
// step - 0.2x2.0x0.3
integer live = 60;

integer i;
integer j;
vector posinit;
default {
```

and that is used for each created object: 
```
llRezObject("floor", llGetPos() + <6.0,-2.0,0.0> + (i%2)*<0.0,2.0,0.0>, <0,0,0>, <0,0,0,0>, live);
```

The goal is to send this parameter to the raised parts so that a second script could do special actions. For example, it can represent the time in seconds that the piece will exist before being automatically deleted. You just need to copy the following script in your walls, floors and steps. After 60 seconds, a red explosion will destroy your blocs one by one.

[Click here to get the script](https://github.com/provirus/tinyapps/tree/master/SecondLifeBuilerHelper/lsl-scripts/scriptkiller.txt)

Do not forget that you can change the value of live to keep your building longer.
This script is very useful when we want to test an architecture in a sandbox without needing to bother to delete our objects before leaving. 
