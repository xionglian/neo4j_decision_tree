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
        System.out.println("xltest5");
        System.out.println("xltest5"+path.endNode());
//        if (path.endNode().hasLabel(Labels.Tree) || (path.endNode().hasLabel(Labels.Rule) && isTrue(path.endNode()))) {
        System.out.println("xltest5"+path.endNode().hasLabel(Labels.Tree));
        System.out.println("xltest5"+path.endNode().hasLabel(Labels.Rule));
        System.out.println("xltest5"+(path.endNode().hasLabel(Labels.Rule)&&isTrue(path.endNode())));

        if(path.endNode().hasLabel(Labels.Tree)){
            System.out.println("xltest7");

            return Evaluation.EXCLUDE_AND_CONTINUE;
        } else if ( (path.endNode().hasLabel(Labels.Rule) && isTrue(path.endNode()))) {
            System.out.println("xltest6");
            return Evaluation.INCLUDE_AND_PRUNE;
        } else {
            System.out.println("xltest8");

            // If not, continue down this path if there is anything else to find.
            return Evaluation.EXCLUDE_AND_CONTINUE;
        }
    }

    @Override
    public Evaluation evaluate(Path path) {
        return null;
    }

    boolean isTrue(Node rule) {
        boolean result = false;
        try{
            Map<String, Object> ruleProperties = rule.getAllProperties();
            for(String key : ruleProperties.keySet()){
                System.out.println(key+":"+ruleProperties.get(key));
            }
            String words = (String) ruleProperties.get("words");
//            String controls = (String) ruleProperties.get("controls");
//            String id = (String) ruleProperties.get("id");
//            String level = (String) ruleProperties.get("level");
            System.out.println("question:"+facts.get("question"));
            System.out.println("words:"+words);
            String question = facts.get("question");
            if(null != question && question.length() > 0 && null != words){
                for(String word : words.split(",")){
                    if(question.contains(word)){
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
