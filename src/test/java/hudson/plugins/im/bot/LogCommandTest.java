package hudson.plugins.im.bot;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import java.util.logging.Logger;

import java.util.List;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Matchers;
import org.mockito.Mockito;
import java.util.Collection;
import java.util.Set;

import hudson.model.AbstractProject;
import hudson.model.AbstractBuild;
import hudson.model.FreeStyleBuild;
import hudson.model.FreeStyleProject;
import hudson.model.Item;
import hudson.model.ItemGroup;
import hudson.model.Run;
import hudson.plugins.im.Sender;
import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.Entry;
import hudson.scm.ChangeLogSet.AffectedFile;
import hudson.scm.RepositoryBrowser;

public class LogCommandTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testLogCommandWithoutParameters1() throws IOException {
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
		
		String replyString = cmd.getReply(bot, sender, new String[] { "log" });
		assertTrue(replyString != null);
	}

	@Test
	public void testChangesCommandWithoutParameters2() throws IOException {
		Bot bot = mock(Bot.class);
		when(bot.getImId()).thenReturn("hudsonbot");

		LogCommand cmd = new LogCommand();
		JobProvider jobProvider = mock(JobProvider.class);
		AbstractProject project = mockProject(jobProvider);
		List<AbstractProject<?, ?>> projects = new ArrayList<AbstractProject<?, ?>>();
		when(jobProvider.getTopLevelJobs()).thenReturn(projects);
		cmd.setJobProvider(jobProvider);
		Sender sender = new Sender("sender");

		String replyString = cmd.getReply(bot, sender, new String[] { "log" });
		assertTrue(replyString.contains("no job found"));
	}
	
	@Test
	public void testLogCommandContainsProjectName() throws IOException {
		Bot bot = mock(Bot.class);
		when(bot.getImId()).thenReturn("hudsonbot");

		LogCommand cmd = new LogCommand();
		JobProvider jobProvider = mock(JobProvider.class);
		AbstractProject project = mockProject(jobProvider);
		List<AbstractProject<?, ?>> projects = new ArrayList<AbstractProject<?, ?>>();
		projects.add(project);
		when(jobProvider.getTopLevelJobs()).thenReturn(projects);
		cmd.setJobProvider(jobProvider);
		when(project.getFullDisplayName()).thenReturn("ProjectName");
		Sender sender = new Sender("sender");

		String replyString = cmd.getReply(bot, sender, new String[] { "log" });
		assertTrue(replyString.contains("ProjectName"));
	}
	
	@Test
	public void testLogCommandContainsNoChanges() throws IOException {
		Bot bot = mock(Bot.class);
		when(bot.getImId()).thenReturn("hudsonbot");

		LogCommand cmd = new LogCommand();
		JobProvider jobProvider = mock(JobProvider.class);
		AbstractProject project = mockProject(jobProvider);
		List<AbstractProject<?, ?>> projects = new ArrayList<AbstractProject<?, ?>>();
		projects.add(project);
		when(jobProvider.getTopLevelJobs()).thenReturn(projects);
		cmd.setJobProvider(jobProvider);
		when(project.getFullDisplayName()).thenReturn("ProjectName");
		Sender sender = new Sender("sender");

		String replyString = cmd.getReply(bot, sender, new String[] { "log" });
		assertTrue(replyString.contains("No changes this build."));
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
		when(build.isBuilding()).thenReturn(false);
		when(project.getLastBuild()).thenReturn(build);
		return project;
	}
} 
