## PositionFix

imageJ plugin for position and round fixation and correction pathological image.

This plugin will enable you to make images from serial section and different stain same position.

![motive](https://github.com/fk506cni/position_fix/blob/master/pf_motiv.png)


![rironnbusou](https://github.com/fk506cni/position_fix/blob/master/arg_ap.png)

## Outline

this plugin fix position and axis of target image as reference image.

this fitting is by mimizing Xor area about binalized image by auto thresholding image.

Fitting algorism is modified Genetic Algorithm(GA).

Process mode
============

## one2one

fixation one target file as one reference file.

## one2multi

(implemented)

fixation multi target file in one directory against one image file.

## sequential fix

(not implemented yet)

sequential fixation. After one fixation, target fix image will be the reference in next fixation.

file names are used as index of order of fixation.

1st file in directory is 1st reference.

## sequential pair

(not implemented yet)

pair fixation. Files in Reference directory and Target directry will be fixed in order.


Args
============

## ref image

reference image. This is used to fix target image. This image file is not modified.

## tag image

image file you want to fix position and axis.

## AT method

This plugin used binalized images to make xor image between two images.

You should choice proper method as shape of object chozen properly.

If selection is not proper, change method.

See Autothresholdin documentation. https://imagej.net/Auto_Threshold

## searchL

This plugin search optimal position(x and y) and slope(theta) by GA.

Searching area in position is - searchL ~ +searchL from position of center of mass matching.

## RoundL and RoundR

Searching area in slope is RoundL ~ RoundR from original slope of target image.

## SaveFormat: f

image saving format.

Equal to SaveAs(f).

If you use Jpeg, quality score should be specified by EDIT > OPTION > INPUT/OUTPUT.

ImageJ's principle does not recommend you to use Jpeg.

See document

https://imagej.net/Principles

## Output dir

Directory where several files saved.

## Random seed

Seed value will be used for SecuareRandom class.

Default is 114514. "IIYO, KOIYO!"

## Check GA param

If you want to tune GA paramters. Check here and tune in next dialog.

# GA parameters

## max population: 300



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