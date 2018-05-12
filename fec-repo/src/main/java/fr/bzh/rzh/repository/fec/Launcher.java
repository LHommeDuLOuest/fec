package fr.bzh.rzh.repository.fec;

@Deprecated
public class Launcher {

	public static void main(String[] args) {
		
		RepoHelper rh = new RepoHelper();
		rh.generateSchema(null);
	}

}
