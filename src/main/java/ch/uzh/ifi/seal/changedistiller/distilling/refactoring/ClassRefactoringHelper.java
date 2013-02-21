package ch.uzh.ifi.seal.changedistiller.distilling.refactoring;

import ch.uzh.ifi.seal.changedistiller.ast.ASTHelper;
import ch.uzh.ifi.seal.changedistiller.model.entities.ClassHistory;
import ch.uzh.ifi.seal.changedistiller.model.entities.StructureEntityVersion;
import ch.uzh.ifi.seal.changedistiller.structuredifferencing.StructureNode;
import ch.uzh.ifi.seal.changedistiller.treedifferencing.matching.measure.LevenshteinSimilarityCalculator;

/**
 * Helps finding refactorings of classes.
 * 
 * @author Beat Fluri, Giacomo Ghezzi
 * @see AbstractRefactoringHelper
 */
public class ClassRefactoringHelper extends AbstractRefactoringHelper {

    /**
     * Creates a new refactoring helper.
     * 
     * @param classHistory
     *            on which the helper creates new {@link StructureEntityVersion}s
     * @param astHelper
     *            to help the refactoring helper
     */
    public ClassRefactoringHelper(ClassHistory classHistory, ASTHelper<StructureNode> astHelper) {
        super(classHistory, astHelper);
        setThreshold(0.65);
    }
    
    @Override
    public StructureEntityVersion createStructureEntityVersion(StructureNode node) {
        return getASTHelper().createInnerClassInClassHistory(getClassHistory(), node);
    }

    @Override
    public StructureEntityVersion createStructureEntityVersionWithID(StructureNode node, String version) {
        return getASTHelper().createInnerClassInClassHistory(getClassHistory(), node, version);
    }
    
    @Override
    public StructureEntityVersion createStructureEntityVersion(StructureNode node, String newEntityName) {
        StructureEntityVersion clazz = createStructureEntityVersion(node);
        if (!node.getFullyQualifiedName().equals(newEntityName)) {
            clazz.setUniqueName(newEntityName);
            getClassHistory().overrideClassHistory(node.getFullyQualifiedName(), newEntityName);
        }
        return clazz;
    }
    
    @Override
    public StructureEntityVersion createStructureEntityVersionWithID(StructureNode node, String newEntityName, String version) {
        StructureEntityVersion clazz = createStructureEntityVersionWithID(node, version);
        if (!node.getFullyQualifiedName().equals(newEntityName)) {
            clazz.setUniqueName(newEntityName);
            getClassHistory().overrideClassHistory(node.getFullyQualifiedName(), newEntityName);
        }
        return clazz;
    }

    @Override
    public String extractShortName(String fullName) {
        return fullName;
    }

    @Override
    public double similarity(StructureNode left, StructureNode right) {
        return new LevenshteinSimilarityCalculator().calculateSimilarity(left.getName(), right.getName());
    }
}