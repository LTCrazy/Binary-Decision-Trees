import java.util.List;
import java.util.ArrayList;

/**
 * Fill in the implementation details of the class DecisionTree using this file. Any methods or
 * secondary classes that you want are fine but we will only interact with those methods in the
 * DecisionTree framework.
 */
public class DecisionTreeImpl {
	public DecTreeNode root;
	public List<List<Integer>> trainData;
	public int maxPerLeaf;
	public int maxDepth;
	public int numAttr;

	// Build a decision tree given a training set
	DecisionTreeImpl(List<List<Integer>> trainDataSet, int mPerLeaf, int mDepth) {
		this.trainData = trainDataSet;
		this.maxPerLeaf = mPerLeaf;
		this.maxDepth = mDepth;
		if (this.trainData.size() > 0) this.numAttr = trainDataSet.get(0).size() - 1;
		this.root = buildTree(trainData, 0);
	}
	
	private DecTreeNode buildTree(List<List<Integer>> currentSet, int depth) {
		// TODO: add code here
		// Detect the maxDepth
		System.out.print("depth: "+depth+"\n");
		if (depth == maxDepth) {
			// Majority vote labels
			return MajorityVote (currentSet);
			}
		// Detect the maxPerLeaf
		if (currentSet.size() <= maxPerLeaf) return MajorityVote (currentSet);
		
		// Neither reach the maxDepth nor maxPerLeaf
		
		// Entropy for current node
		int countC1 = 0;
		double currentEntropy;
		for (int i = 0; i < currentSet.size(); i++) {
			if (label(currentSet.get(i)) == 0) countC1+=1;
		}
		double p1 = (double)countC1/(double)currentSet.size();
//		System.out.print("p1: "+p1+"\n");
		if (p1 == 0 || p1 == 1) {
			return MajorityVote (currentSet);
		}
		else currentEntropy = -p1 * (Math.log(p1)/ Math.log(2)) - (1 - p1) * (Math.log(1 - p1)/ Math.log(2));
		System.out.print("Entropy for current node: "+currentEntropy+"\n");
		
		double MinEntropy = Double.POSITIVE_INFINITY;
		int attribute = -99;
		int threshold = -99;
		DecTreeNode currentNode;
		// For each attribute
		for (int i = 0; i < numAttr; i++) {
			// For each possible split	
//			double MinEntro4TheAttr = Double.POSITIVE_INFINITY;
			for (int j = 1; j <= 9; j++) {
				System.out.print("split: "+j+"\n");
				//Entropy for each split
				int countLeftC1 = 0;
				int countLeftC2 = 0;
				int countRightC1 = 0;
				int countRightC2 = 0;
				double pLeftC1 = 0.0;
				double pLeftC2 = 0.0;
				double pRightC1 = 0.0;
				double pRightC2 = 0.0;
				double entropyLeft = -99;
				double entropyRight = -99;
				for (int w = 0; w < currentSet.size(); w++) {
					if (currentSet.get(w).get(i) <= j && label(currentSet.get(w)) == 0) countLeftC1++;
					else if (currentSet.get(w).get(i) <= j && label(currentSet.get(w)) == 1) countLeftC2++;
					else if (currentSet.get(w).get(i) > j && label(currentSet.get(w)) == 0) countRightC1++;
					else countRightC2++;
				}
//				System.out.print("countLeftC1: "+countLeftC1+"\n");
//				System.out.print("countLeftC2: "+countLeftC2+"\n");
//				System.out.print("countRightC1: "+countRightC1+"\n");
//				System.out.print("countRightC2: "+countRightC2+"\n");
				
				// Left side
				if ((countLeftC1+countLeftC2) == 0) {
					entropyLeft = 0;
				}
				else {
					pLeftC1 = (double)countLeftC1/((double)countLeftC1+(double)countLeftC2);
					pLeftC2 = 1 - pLeftC1;
					if (pLeftC2 == 0.0) {
						entropyLeft = -pLeftC1*(Math.log(pLeftC1)/Math.log(2));
					}
					else if (pLeftC1 == 0.0) {
						entropyLeft = -pLeftC2*(Math.log(pLeftC2)/Math.log(2));
					}
					else {
						entropyLeft = -pLeftC1*(Math.log(pLeftC1)/Math.log(2))-pLeftC2*(Math.log(pLeftC2)/Math.log(2));
					}
				}
				// Right side
				if ((countRightC1+countRightC2) == 0) {
					entropyRight = 0;
				}
				else {
					pRightC1 = (double)countRightC1/((double)countRightC1+(double)countRightC2);
					pRightC2 = 1 - pRightC1;
					if (pRightC2 == 0.0) {
						entropyRight = -pRightC1*(Math.log(pRightC1)/Math.log(2));
					}
					else if (pRightC1 == 0.0) {
						entropyRight = -pRightC2*(Math.log(pRightC2)/Math.log(2));
					}
					else {
						entropyRight = -pRightC1*(Math.log(pRightC1)/Math.log(2))-pRightC2*(Math.log(pRightC2)/Math.log(2));
					}
				}
				double entropy = entropyLeft*(((double)countLeftC1+(double)countLeftC2)/(double)currentSet.size()) 
						+ entropyRight*(((double)countRightC1+(double)countRightC2)/(double)currentSet.size());
				System.out.print("entropy: "+entropy+"\n");
				if (entropy < MinEntropy) {
					System.out.print("MinEntropy (too mini): "+MinEntropy+"\n");
					MinEntropy = entropy;
					attribute = i;
					System.out.print("attribute: "+attribute+"\n");
					threshold = j;
					System.out.print("threshold: "+threshold+"\n");
					System.out.print("Info gain: "+(currentEntropy - MinEntropy)+"\n");
				}
			}
//			if (MinEntro4TheAttr < MinEntropy) MinEntropy = MinEntro4TheAttr;
		}
		// Info gain is 0; Majority vote
		if ((currentEntropy - MinEntropy) <= 0 ) {
			return MajorityVote (currentSet);
		}
		else {
			// Build current node
			currentNode = new DecTreeNode (-99, attribute, threshold);
			
			// Create left and right children
			List<List<Integer>> leftSet = new ArrayList<List<Integer>>();
			List<List<Integer>> rightSet = new ArrayList<List<Integer>>();
			for (int i = 0; i < currentSet.size(); i++) {
				if (currentSet.get(i).get(attribute) <= threshold ) leftSet.add(currentSet.get(i));
				else rightSet.add(currentSet.get(i));
			}
			currentNode.left = buildTree(leftSet, depth+1);
			currentNode.right = buildTree(rightSet, depth+1);
		}
		return currentNode;
	}
	
	public DecTreeNode MajorityVote (List<List<Integer>> currentSet) {
		int countC2 = 0;
		for (int i = 0; i < currentSet.size(); i++) {
			if (label(currentSet.get(i))==1) countC2++;
		}
		if (countC2 >= (currentSet.size() - countC2)) return new DecTreeNode (1, 0, 0);
		else return new DecTreeNode (0, 0, 0);
	}
	
	public int label(List<Integer> instance) {
		return instance.get(instance.size() - 1);
	}
	
	public int classify(List<Integer> instance) {
		// TODO: add code here
		// Note that the last element of the array is the label.
		DecTreeNode node = root;
		while (!node.isLeaf()) {
			if (instance.get(node.attribute) <= node.threshold) node = node.left;
			else node = node.right;
		}
		return node.classLabel;
	}

//	public int classifyHelper(List<Integer> instance, DecTreeNode node) {
//		if (instance.get(node.attribute) <= node.threshold) classifyHelper(instance, node.left);
//		else classifyHelper(instance, node.right);
//		return 0;
//	}
	
	// Print the decision tree in the specified format
	public void printTree() {
		printTreeNode("", this.root);
	}

	public void printTreeNode(String prefixStr, DecTreeNode node) {
		String printStr = prefixStr + "X_" + node.attribute;
		System.out.print(printStr + " <= " + String.format("%d", node.threshold));
		if(node.left.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.left.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.left);
		}
		System.out.print(printStr + " > " + String.format("%d", node.threshold));
		if(node.right.isLeaf()) {
			System.out.println(" : " + String.valueOf(node.right.classLabel));
		}
		else {
			System.out.println();
			printTreeNode(prefixStr + "|\t", node.right);
		}
	}
	
	public double printTest(List<List<Integer>> testDataSet) {
		int numEqual = 0;
		int numTotal = 0;
		for (int i = 0; i < testDataSet.size(); i ++)
		{
			int prediction = classify(testDataSet.get(i));
			int groundTruth = testDataSet.get(i).get(testDataSet.get(i).size() - 1);
			System.out.println(prediction);
			if (groundTruth == prediction) {
				numEqual++;
			}
			numTotal++;
		}
		double accuracy = numEqual*100.0 / (double)numTotal;
		System.out.println(String.format("%.2f", accuracy) + "%");
		return accuracy;
	}
}
