package net.thucydides.core.annotations;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableList;
import net.thucydides.core.reports.html.Formatter;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static net.thucydides.core.util.NameConverter.withNoArguments;

/**
 * Utility class used to help process annotations on tests and test steps.
 */
public class TestAnnotations {

    private final Class<?> testClass;

    private TestAnnotations(final Class<?> testClass) {
        this.testClass = testClass;
    }

    public static TestAnnotations forClass(final Class<?> testClass) {
        return new TestAnnotations(testClass);
    }

    public Optional<String> getAnnotatedTitleForMethod(final String methodName) {
        if ((testClass != null) && (testClassHasMethodCalled(methodName))) {
            return getAnnotatedTitle(methodName);
        }
        return Optional.absent();
    }

    public boolean isPending(final String methodName) {
        Optional<Method> method = getMethodCalled(methodName);
        return method.isPresent() && isPending(method.get());
    }

    public static boolean isPending(final Method method) {
        return method != null && (method.getAnnotation(Pending.class) != null);
    }

    public static boolean isIgnored(final Method method) {
        if (method != null) {
            return hasAnnotationCalled(method, "Ignore");
        }
        return false;
    }

    private static boolean hasAnnotationCalled(Method method, String annotationName) {
        Annotation[] annotations = method.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getSimpleName().equals(annotationName)) {
                return true;
            }
        }
        return false;
    }

    public boolean isIgnored(final String methodName) {
        Optional<Method> method = getMethodCalled(methodName);
        return method.isPresent() && isIgnored(method.get());
    }

    private Optional<String> getAnnotatedTitle(String methodName) {
        Optional<Method> testMethod = getMethodCalled(methodName);
        if (testMethod.isPresent()) {
            Title titleAnnotation = testMethod.get().getAnnotation(Title.class);
            if (titleAnnotation != null) {
                return Optional.of(titleAnnotation.value());
            }
        }
        return Optional.absent();
    }

    private boolean testClassHasMethodCalled(final String methodName) {
        return (getMethodCalled(methodName).isPresent());

    }

    private Optional<Method> getMethodCalled(final String methodName) {
        if (testClass == null) {
            return Optional.absent();
        }

        String baseMethodName = withNoArguments(methodName);
        try {
            return Optional.of(testClass.getMethod(baseMethodName));
        } catch (NoSuchMethodException e) {
            return Optional.absent();
        }
    }

    /**
     * Return a list of the issues mentioned in the title annotation of this method.
     */
    public List<String> getAnnotatedIssuesForMethodTitle(String methodName) {
        Optional<String> title = getAnnotatedTitleForMethod(methodName);
        if (title.isPresent()) {
            return Formatter.issuesIn(title.get());
        } else {
            return Formatter.issuesIn(methodName);
        }
    }


    private Optional<String> getAnnotatedIssue(String methodName) {
        Optional<Method> testMethod = getMethodCalled(methodName);
        if ((testMethod.isPresent()) && (testMethod.get().getAnnotation(Issue.class) != null)) {
            return Optional.of(testMethod.get().getAnnotation(Issue.class).value());
        }
        return Optional.absent();
    }

    private String[] getAnnotatedIssues(String methodName) {
        Optional<Method> testMethod = getMethodCalled(methodName);
        if ((testMethod.isPresent()) && (testMethod.get().getAnnotation(Issues.class) != null)) {
            return testMethod.get().getAnnotation(Issues.class).value();
        }
        return new String[]{};
    }

    /**
     * Return a list of the issues mentioned in the Issue annotation of this method.
     * @param methodName the name of the test method in the Java test class, if applicable.
     * returns 
     */
    public Optional<String> getAnnotatedIssueForMethod(String methodName) {
        return getAnnotatedIssue(methodName);
    }

    public String[] getAnnotatedIssuesForMethod(String methodName) {
        return getAnnotatedIssues(methodName);
    }

    public String getAnnotatedIssueForTestCase(Class<?> testCase) {
        Issue issueAnnotation = testCase.getAnnotation(Issue.class);
        if (issueAnnotation != null) {
            return issueAnnotation.value();
        } else {
            return null;
        }
    }

    public String[] getAnnotatedIssuesForTestCase(Class<?> testCase) {
        Issues issueAnnotation = testCase.getAnnotation(Issues.class);
        if (issueAnnotation != null) {
            return issueAnnotation.value();
        } else {
            return null;
        }
    }

    public List<String> getIssuesForMethod(String methodName) {
        List<String> issues = new ArrayList<String>();

        if (testClass != null) {
            addIssuesFromMethod(methodName, issues);
        } else {
            addIssuesFromTestScenarioName(methodName, issues);
        }
        return issues;
    }

    private void addIssuesFromTestScenarioName(String methodName, List<String> issues) {
        issues.addAll(getAnnotatedIssuesForMethodTitle(methodName));
    }

    private void addIssuesFromMethod(String methodName, List<String> issues) {
        if (getAnnotatedIssues(methodName) != null) {
            issues.addAll(Arrays.asList(getAnnotatedIssues(methodName)));
        }

        if (getAnnotatedIssue(methodName).isPresent()) {
            issues.add(getAnnotatedIssue(methodName).get());
        }

        if (getAnnotatedTitle(methodName) != null) {
            addIssuesFromTestScenarioName(methodName, issues);
        }
    }

    public List<WithTag> getTagsForMethod(String methodName) {

        List<WithTag> allTags = new ArrayList<WithTag>(getTags());
        allTags.addAll(getTagsFor(methodName));

        return ImmutableList.copyOf(allTags);
    }

    public List<WithTag> getTags() {
        List<WithTag> tags = new ArrayList<WithTag>();
        if (testClass != null) {
            addTags(tags, testClass.getAnnotation(WithTags.class));
            addTag(tags, testClass.getAnnotation(WithTag.class));
        }
        return tags;
    }

    private void addTag(List<WithTag> tags, WithTag tag) {
        if (tag != null) {
            tags.add(tag);
        }
    }

    private void addTags(List<WithTag> tags, WithTags tagSet) {
        if (tagSet != null) {
            tags.addAll(Arrays.asList(tagSet.value()));
        }
    }

    private List<WithTag> getTagsFor(String methodName) {
        List<WithTag> tags = new ArrayList<WithTag>();

        Optional<Method> testMethod = getMethodCalled(methodName);
        if (testMethod.isPresent()) {
            addTags(tags, testMethod.get().getAnnotation(WithTags.class));
            addTag(tags, testMethod.get().getAnnotation(WithTag.class));
        }
        return tags;
    }

}
