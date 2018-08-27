
package com.garfield.lint;

import com.android.tools.lint.detector.api.Issue;
import com.garfield.lint.issue.NewThreadDetector;
import com.garfield.lint.issue.ToastUtilsDetector;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class IssueRegistry extends com.android.tools.lint.client.api.IssueRegistry {
    @Override
    public List<Issue> getIssues() {
        return Arrays.asList(NewThreadDetector.ISSUE, ToastUtilsDetector.ISSUE);
    }

    @Override
    public int getApi() {
        return com.android.tools.lint.detector.api.ApiKt.CURRENT_API;
    }
}
