package net.thucydides.core.statistics.service;

import ch.lambdaj.function.convert.Converter;
import com.google.common.collect.Sets;
import net.thucydides.core.Thucydides;
import net.thucydides.core.ThucydidesSystemProperty;
import net.thucydides.core.annotations.TestAnnotations;
import net.thucydides.core.annotations.TestCaseAnnotations;
import net.thucydides.core.annotations.WithTag;
import net.thucydides.core.guice.Injectors;
import net.thucydides.core.model.TestOutcome;
import net.thucydides.core.model.TestTag;
import net.thucydides.core.statistics.model.TestRunTag;
import net.thucydides.core.util.EnvironmentVariables;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static ch.lambdaj.Lambda.convert;
import static ch.lambdaj.Lambda.project;

public class AnnotationBasedTagProvider implements TagProvider {
    
    public AnnotationBasedTagProvider() {
    }

    public Set<TestTag> getTagsFor(final TestOutcome testOutcome) {
        if (testOutcome.getTestCase() == null) {
            return Collections.emptySet();
        }
        List<WithTag> tags = TestAnnotations.forClass(testOutcome.getTestCase()).getTagsForMethod(testOutcome.getMethodName());

        return Sets.newHashSet(convert(tags, toTestTags()));
    }

    private Converter<Object, TestTag> toTestTags() {
        return new Converter<Object, TestTag>() {

            @Override
            public TestTag convert(Object tag) {
                WithTag withTag = (WithTag) tag;
                return TestTag.withName(withTag.name()).andType(withTag.type());
            }
        };
    }
}
