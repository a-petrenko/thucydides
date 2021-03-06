// Verify story printing
htmlReport = new File("${basedir}/target/site/thucydides/index.html")
assert htmlReport.exists()
def reportContent = htmlReport.text
assert reportContent.contains("<a href=\"http://my.jira.server/browse/MYPROJECT-123\">#123</a> and <a href=\"http://my.jira.server/browse/MYPROJECT-456\">#456</a>")

def testReport = new File("${basedir}/target/site/thucydides/sample_test_scenario_happy_day_scenario.html").text
assert testReport.contains("fixes issues <a href=\"http://my.jira.server/browse/MYPROJECT-123\">#123</a> and <a href=\"http://my.jira.server/browse/MYPROJECT-456\">#456</a>")
assert testReport.contains("fixes issues <a href=\"http://my.jira.server/browse/MYPROJECT-123\">#123</a> and <a href=\"http://my.jira.server/browse/MYPROJECT-456\">#456</a>")
