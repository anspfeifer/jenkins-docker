import com.cloudbees.jenkins.plugins.sshcredentials.impl.*
import com.cloudbees.jenkins.plugins.sshcredentials.impl.*;
import com.cloudbees.plugins.credentials.*
import com.cloudbees.plugins.credentials.*;
import com.cloudbees.plugins.credentials.common.*
import com.cloudbees.plugins.credentials.domains.*
import com.cloudbees.plugins.credentials.domains.*;
import com.cloudbees.plugins.credentials.impl.*
import com.cloudbees.plugins.credentials.impl.*;
import hudson.plugins.sshslaves.*;
import jenkins.model.*;
import org.jenkinsci.plugins.plaincredentials.impl.*;
import hudson.security.*;
import hudson.security.csrf.*;

if (!System.getenv('NO_BOOTSTRAP')) {
	def instance = Jenkins.getInstance()

	def strategy = new FullControlOnceLoggedInAuthorizationStrategy()
	strategy.setAllowAnonymousRead(true)
	def realm = new HudsonPrivateSecurityRealm(false, false, null)
	instance.setAuthorizationStrategy(strategy)
	instance.setSecurityRealm(realm)
	instance.setCrumbIssuer(null)

	hudson.model.User admin = hudson.model.User.getById('admin', true)
	admin.setFullName('admin')
	def email_param = new hudson.tasks.Mailer.UserProperty('matthew.arturi@symphony.com')
	admin.addProperty(email_param)
	def secret = System.getenv('JENKINS_OPTS')
    def parts = secret.tokenize()
    def pwd = ''
    for (String part : parts) {
        if (part.indexOf('--argumentsRealm.passwd.jenkins') > -1) {
            pwd = part.tokenize('=')[1]
        }
    }
	def pw_param = hudson.security.HudsonPrivateSecurityRealm.Details.fromPlainPassword(pwd)
	admin.addProperty(pw_param)
	admin.save()
}
