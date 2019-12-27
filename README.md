# Binary-Decision-Trees

This project implements a program that builds a binary decision tree for numerical attributes,
and binary classification tasks. By binary tree, we mean that every non-leaf node of your tree will have
exactly two children. Each node will have a selected attribute and an associated threshold value.
Instances (aka examples) that have an attribute value less than or equal to the threshold belong to the
left subtree of a node, and instances with an attribute value greater than the threshold belong to the right
subtree of a node. 

Input:  java HW3 <train file> <test file> <maximum instances per leaf> <maximum depth>
