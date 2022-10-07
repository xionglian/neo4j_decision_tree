package com.maxdemarzi;

import com.maxdemarzi.schema.Labels;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.graphdb.traversal.Evaluation;
import org.neo4j.graphdb.traversal.PathEvaluator;

import java.util.Map;

public class DecisionTreeEvaluator implements PathEvaluator {
    private Map<String, String> facts;
    public DecisionTreeEvaluator(Map<String, String> facts){
        this.facts = facts;

    }
    @Override
    public Evaluation evaluate(Path path, BranchState branchState) {
        // If we get to an Answer stop traversing, we found a valid path.
//        if (path.endNode().hasLabel(Labels.Tree) || (path.endNode().hasLabel(Labels.Rule) && isTrue(path.endNode()))) {
        System.out.println("========in evaluate===========");
        Evaluation result = Evaluation.EXCLUDE_AND_CONTINUE;
        System.out.println("path:"+path.toString());

        if(path.endNode().hasLabel(Labels.Tree)){
            System.out.println("label is tree");
            result = Evaluation.EXCLUDE_AND_CONTINUE;
        } else if ((path.endNode().hasLabel(Labels.Rule) && isTrue(path.endNode()))) {
            System.out.println("label is rule, and isTrue = true");
            result = Evaluation.INCLUDE_AND_CONTINUE;
        } else {
            System.out.println("others");
        }
        System.out.println("========out evaluate===========");
        return result;

    }

    @Override
    public Evaluation evaluate(Path path) {
        return null;
    }

    boolean isTrue(Node rule) {
        boolean result = false;
        try{
            Map<String, Object> ruleProperties = rule.getAllProperties();
//            for(String key : ruleProperties.keySet()){
//                System.out.println(key+":"+ruleProperties.get(key));
//            }
            String words = (String) ruleProperties.get("words");
            System.out.println("question:"+facts.get("question"));
            System.out.println("words:"+words);
            String question = facts.get("question");
            if(null != question && question.length() > 0 && null != words){
                for(String word : words.split(",")){
                    if(question.contains(word.trim())){
                        result = true;
                        break;
                    }
                }
            }
        }catch (Exception e){

        }




        return result;
    }
}
