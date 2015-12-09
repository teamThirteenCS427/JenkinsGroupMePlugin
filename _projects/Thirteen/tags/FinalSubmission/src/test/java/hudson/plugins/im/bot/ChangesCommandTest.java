package hudson.plugins.im.bot;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import hudson.model.AbstractBuild;
import hudson.model.AbstractProject;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.ItemGroup;
import hudson.model.Run;
import hudson.plugins.im.Sender;
import hudson.scm.ChangeLogSet;

public class ChangesCommandTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testChangesCommandWithoutParameters1() throws IOException {
		Bot bot = mock(Bot.class);
		when(bot.getImId()).thenReturn("hudsonbot");

		ChangesCommand cmd = new ChangesCommand();
		JobProvider jobProvider = mock(JobProvider.class);
		AbstractProject project = mockProject(jobProvider);
		List<AbstractProject<?, ?>> projects = new ArrayList<AbstractProject<?, ?>>();
		projects.add(project);
		when(jobProvider.getTopLevelJobs()).thenReturn(projects);
		cmd.setJobProvider(jobProvider);

		Sender sender = new Sender("sender");

		String replyString = cmd.getReply(bot, sender, new String[] { "changes" });
		assertTrue(replyString.contains("changes of all projects:"));
	}

	@Test
	public void testChangesCommandWithoutParameters2() throws IOException {
		Bot bot = mock(Bot.class);
		when(bot.getImId()).thenReturn("hudsonbot");
		ChangesCommand cmd = new ChangesCommand();
		JobProvider jobProvider = mock(JobProvider.class);
		AbstractProject project = mockProject(jobProvider);
		List<AbstractProject<?, ?>> projects = new ArrayList<AbstractProject<?, ?>>();
		when(jobProvider.getTopLevelJobs()).thenReturn(projects);
		cmd.setJobProvider(jobProvider);
		Sender sender = new Sender("sender");

		String replyString = cmd.getReply(bot, sender, new String[] { "changes" });
		assertTrue(replyString.contains("no job found"));
	}

	@Test
	public void TestGetMessageForJob1() {
		ChangesCommand cmd = new ChangesCommand();
		AbstractProject project = mock(FreeStyleProject.class);
		ItemGroup parent = mock(ItemGroup.class);
		when(parent.getFullDisplayName()).thenReturn("");
		when(project.getParent()).thenReturn(parent);
		when(project.getLastBuild()).thenReturn(null);
		String replyString = cmd.getMessageForJob(project).toString();
		assertTrue(replyString.contains("no finished build"));
	}

	@Test
	public void TestGetMessageForJob2() {
		ChangesCommand cmd = new ChangesCommand();
		AbstractProject project = mock(FreeStyleProject.class);
		AbstractBuild lastBuild = mock(FreeStyleBuild.class);
		ItemGroup parent = mock(ItemGroup.class);
		when(parent.getFullDisplayName()).thenReturn("");
		when(project.getParent()).thenReturn(parent);
		when(project.getLastBuild()).thenReturn(lastBuild);
		when(lastBuild.isBuilding()).thenReturn(false);
		Run run = mock(Run.class);
		ChangeLogSet changeSet = ChangeLogSet.createEmpty(run);
		when(lastBuild.getChangeSet()).thenReturn(changeSet);
		when(lastBuild.getPreviousBuild()).thenReturn(null);
		String replyString = cmd.getMessageForJob(project).toString();
		assertTrue(replyString.contains("No changes"));
	}

	@Test(expected = NullPointerException.class)
	public void TestGetMessageForJob3() {
		ChangesCommand cmd = new ChangesCommand();
		AbstractProject project = mock(FreeStyleProject.class);
		AbstractBuild lastBuild = mock(FreeStyleBuild.class);
		ItemGroup parent = mock(ItemGroup.class);
		when(parent.getFullDisplayName()).thenReturn("");
		when(project.getParent()).thenReturn(parent);
		when(project.getLastBuild()).thenReturn(lastBuild);
		when(lastBuild.isBuilding()).thenReturn(false);
		Run run = mock(Run.class);
		ChangeLogSet changeSet = mock(ChangeLogSet.class);
		when(lastBuild.getChangeSet()).thenReturn(changeSet);
		when(changeSet.isEmptySet()).thenReturn(false);
		ChangeLogSet.Entry firstEntry = mock(ChangeLogSet.Entry.class);
		String str = cmd.getMessageForJob(project).toString();
	}

	@Test
	public void TestGetMessageForJobWithBuildNum1() {
		ChangesCommand cmd = new ChangesCommand();
		AbstractProject project = mock(FreeStyleProject.class);
		ItemGroup parent = mock(ItemGroup.class);
		when(parent.getFullDisplayName()).thenReturn("");
		when(project.getParent()).thenReturn(parent);
		int num = 0;
		when(project.getBuildByNumber(num)).thenReturn(null);
		String replyString = cmd.getMessageForJobWithBuildNum(project, num).toString();
		assertTrue(replyString.contains("doesn't exist"));
	}

	@Test
	public void TestGetMessageForJobWithBuildNum2() {
		int num = 3;
		ChangesCommand cmd = new ChangesCommand();
		AbstractProject project = mock(FreeStyleProject.class);
		ItemGroup parent = mock(ItemGroup.class);
		when(parent.getFullDisplayName()).thenReturn(" ");
		when(project.getParent()).thenReturn(parent);
		AbstractBuild build = mock(FreeStyleBuild.class);
		when(project.getBuildByNumber(num)).thenReturn(build);
		when(build.isBuilding()).thenReturn(true);
		String replyString = cmd.getMessageForJobWithBuildNum(project, num).toString();
		assertTrue(replyString.contains("currently building"));
	}

	@Test
	public void TestGetMessageForJobWithBuildNum3() {
		int num = 3;
		ChangesCommand cmd = new ChangesCommand();
		AbstractProject project = mock(FreeStyleProject.class);
		ItemGroup parent = mock(ItemGroup.class);
		when(parent.getFullDisplayName()).thenReturn(" ");
		when(project.getParent()).thenReturn(parent);
		AbstractBuild build = mock(FreeStyleBuild.class);
		when(project.getBuildByNumber(num)).thenReturn(build);
		when(build.isBuilding()).thenReturn(false);
		ChangeLogSet changeSet = mock(ChangeLogSet.class);
		when(build.getChangeSet()).thenReturn(changeSet);
		when(changeSet.isEmptySet()).thenReturn(true);
		String replyString = cmd.getMessageForJobWithBuildNum(project, num).toString();
		assertTrue(replyString.contains("No changes"));
	}

	@Test(expected = NullPointerException.class)
	public void TestGetMessageForJobWithBuildNum4() {
		int num = 3;
		ChangesCommand cmd = new ChangesCommand();
		AbstractProject project = mock(FreeStyleProject.class);
		ItemGroup parent = mock(ItemGroup.class);
		when(parent.getFullDisplayName()).thenReturn(" ");
		when(project.getParent()).thenReturn(parent);
		AbstractBuild build = mock(FreeStyleBuild.class);
		when(project.getBuildByNumber(num)).thenReturn(build);
		when(build.isBuilding()).thenReturn(false);
		ChangeLogSet changeSet = mock(ChangeLogSet.class);
		when(build.getChangeSet()).thenReturn(changeSet);
		when(changeSet.isEmptySet()).thenReturn(false);
		String replyString = cmd.getMessageForJobWithBuildNum(project, num).toString();
	}

	@SuppressWarnings("unchecked")
	private AbstractProject<?, ?> mockProject(JobProvider jobProvider) {
		@SuppressWarnings("rawtypes")
		AbstractProject project = mock(FreeStyleProject.class);
		ItemGroup parent = mock(ItemGroup.class);
		FreeStyleBuild build = mock(FreeStyleBuild.class);
		Run run = mock(Run.class);
		ChangeLogSet changeSet = ChangeLogSet.createEmpty(run);

		when(parent.getFullDisplayName()).thenReturn("");
		when(project.getParent()).thenReturn(parent);
		when(build.getChangeSet()).thenReturn(changeSet);
		return project;
	}

}
