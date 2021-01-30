package org.uushang.boot.auth.environment;

public class InformationSource {
	private Project project;

	public void setProject(Project project) {
		this.project = project;
	}

	public String toString() {
		return "InformationSource(project=" + getProject() + ")";
	}

	public Project getProject() {
		return this.project;
	}
}
