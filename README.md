## PositionFix

imageJ plugin for position and round fixation and correction pathological image.

This plugin will enable you to make images from serial section and different stain same position.

![motive](https://github.com/fk506cni/position_fix/blob/master/pf_motiv.png)


![rironnbusou](https://github.com/fk506cni/position_fix/blob/master/arg_ap.png)

## Outline

this plugin fix position and axis of target image as reference image.

this fitting is by mimizing Xor area about binalized image by auto thresholding image.

Fitting algorism is modified Genetic Algorithm(GA).

Args
============

## ref image

reference image. This is used to fix target image. This image file is not modified.

## tag image

image file you want to fix position and axis.





## GA parameters

### max population: xxx

### process generation : xx

### encoding: real encoding(position x, y is int, and axis theta is double)

### selection: ranking selection

### cross over: uniform cross over or random mix of parents' genes.

### mutation:

gene_tabulation:(100.0+this.rand.nextGaussian())/100)*range

genome_mutation: new val in range from uniform distribution.

### preserved parents rate

### invaders rate

### children rate

These 3 population will make next generation.


## Caution

In GA, algorism require large time if this use swap.

Be careful about memory setting.

## Processing dilemma

Rotation of image is not reversible transformation.

If you want to conserve target infomation as long as can, you should prepera large images.

However large images require long processing time.


License
============

Plugin: GPLv3

Cartoons above: from irasutoya, "http://www.irasutoya.com/". Copyright is belong to Irasutoya.




Written by

fk506cni == unkodaisuki!

since 2018.3



## Cause of trouble

(Sorry. This section is contain troubles which I cant overcome in plugin.)

### Image selection

if image file contain selection information. Auto Thresholding is done in selected area.




Citation
============

Under construction