package hudson.plugins.im.bot;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

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

public class ChangesCommandTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testChangesCommandWithoutParameters1() throws IOException {
		Bot bot = mock(Bot.class);
        when(bot.getImId()).thenReturn("hudsonbot");
        
        ChangesCommand cmd = new ChangesCommand();
//        String projectName = "project name with spaces";
//        AbstractProject project = mock(AbstractProject.class);
        JobProvider jobProvider = mock(JobProvider.class);
        AbstractProject project = mockProject(jobProvider);
        List<AbstractProject<?,?>> projects = new ArrayList<AbstractProject<?,?>>();
        projects.add(project);
        when(jobProvider.getTopLevelJobs()).thenReturn(projects);
        cmd.setJobProvider(jobProvider);
        
        Sender sender = new Sender("sender");
        
        String replyString = cmd.getReply(bot, sender, new String[]{ "changes"});
        assertTrue(replyString.startsWith("changes of all projects:"));
	}
	
	
	//test for no last build
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
	public void TestGetMessageForJobWithBuildNum1(){
		ChangesCommand cmd = new ChangesCommand();		
		AbstractProject project = mock(FreeStyleProject.class);	
		ItemGroup parent = mock(ItemGroup.class);
		when(parent.getFullDisplayName()).thenReturn("");
		when(project.getParent()).thenReturn(parent);
		int num = 0;
		when(project.getBuildByNumber(num)).thenReturn(null);
		String replyString = cmd.getMessageForJobWithBuildNum(project,num).toString();
		assertTrue(replyString.contains("no such build"));
	}

	@Test
	public void TestGetMessageForJobWithBuildNum2(){
		int num = 3;
		ChangesCommand cmd = new ChangesCommand();		
		AbstractProject project = mock(FreeStyleProject.class);
		ItemGroup parent = mock(ItemGroup.class);
		when(parent.getFullDisplayName()).thenReturn(" ");
		when(project.getParent()).thenReturn(parent);
		AbstractBuild build = mock(FreeStyleBuild.class);
		when(project.getBuildByNumber(num)).thenReturn(build);
		when(build.isBuilding()).thenReturn(true);
		String replyString = cmd.getMessageForJobWithBuildNum(project,num).toString();
		assertTrue(replyString.contains("currently building"));
	}


	@SuppressWarnings("unchecked")
    private AbstractProject<?, ?> mockProject(JobProvider jobProvider) {
        @SuppressWarnings("rawtypes")
        AbstractProject project = mock(FreeStyleProject.class);
        ItemGroup parent = mock(ItemGroup.class);
        FreeStyleBuild build = mock(FreeStyleBuild.class);
        Run run = mock(Run.class);
        ChangeLogSet changeSet = ChangeLogSet.createEmpty(run);
        
//		when(build.getUrl()).thenReturn("job/foo/32/");
		when(parent.getFullDisplayName()).thenReturn("");
//        when(jobProvider.getJobByNameOrDisplayName(Matchers.anyString())).thenReturn(project);
//        when(project.hasPermission(Item.BUILD)).thenReturn(Boolean.TRUE);
//        when(project.isBuildable()).thenReturn(true);
        when(project.getParent()).thenReturn(parent);
//        when(project.getFullDisplayName()).thenReturn("fsProject");
//        when(project.getLastBuild()).thenReturn(build);
//        when(changeSet.getItems()).thenReturn(new Object[0]); //Doesn't work because mockito can not mock final methods
        when(build.getChangeSet()).thenReturn(changeSet);
        return project;
    }

}
