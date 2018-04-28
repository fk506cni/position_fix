## PositionFix

imageJ plugin for position and round fixation and correction pathological image.

This plugin will enable you to make images from serial section and different stain same position.

![motive](https://github.com/fk506cni/position_fix/blob/master/pf_motiv.png)


## Outline

this plugin fix position and axis of target image as reference image.

this fitting is by mimizing Xor area about binalized image by auto thresholding image.

Fitting algorism is modified Genetic Algorithm(GA).

GA parameters is bellow

max population: xxx

process generation : xx

encoding: real encoding(position x, y is int, axis theta is double)

selection: ranking selection

cross over: uniform cross over or random mix of parents' genes.

mutation:

gene_tabulation:(100.0+this.rand.nextGaussian())/100)*range

genome_mutation: new val in range from uniform distribution.




## under construction