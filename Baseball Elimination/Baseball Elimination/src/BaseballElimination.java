import java.util.TreeSet;


public class BaseballElimination {
	private int numberOfTeams = 0;
	private int[] w;
	private int[] l;
	private int[] r;
	private int[][] g;
	private String[] teams;
	private FordFulkerson result;
	private TreeSet<String> isEliminatedTeamName;
	public BaseballElimination(String filename) {
		In in = new In(filename);
		if (!in.isEmpty())
			numberOfTeams = Integer.parseInt(in.readLine());
		w = new int[numberOfTeams];
		l = new int[numberOfTeams];
		r = new int[numberOfTeams];
		g = new int[numberOfTeams][numberOfTeams];
		teams = new String[numberOfTeams];
		
		String s;
		String s2;
		int i = 0;
		while (!in.isEmpty()) {
			s = in.readLine();
			int p1 = 0;
			int p2 = 0;
			while (s.charAt(p2) == ' ')
				p2++;
			p1 = p2;
			while (s.charAt(p2) != ' ')
				p2++;
			teams[i] = s.substring(p1, p2); // get the location of the end of team name
			while (s.charAt(p2) == ' ') //get the location of the beginning of data
				p2++;			
			s2 = s.substring(p2);
			int head = 0;
			int tail = 0;
			int[] a = new int[numberOfTeams + 3];
			int j = 0;
			while (tail < s2.length()) {// allocate data into w[],l[],r[],g[][]
				if (s2.charAt(tail) != ' ') {
					if (tail == s2.length() - 1) {
						a[j++] = Integer.parseInt(s2.substring(head, tail + 1));
					}
					tail++;
					continue;
				}

				if (s2.charAt(head) != ' ') {
					a[j++] = Integer.parseInt(s2.substring(head, tail));
					tail++;
					head = tail;
				}
				else {
					tail++;
					head = tail;
					continue;
				}		
			}
			//a[j++] = Integer.parseInt(s2.substring(head, tail));
			w[i] = a[0];
			l[i] = a[1];
			r[i] = a[2];
			for (int m = 0; m < numberOfTeams; m++)
				g[i][m] = a[m + 3];
			i++;
		}	
	}
	
	public int numberOfTeams() {
		return this.numberOfTeams;
	}
	
	public Iterable<String> teams() {
		Queue<String> teamOfName = new Queue<String>();
		for (int i = 0; i < numberOfTeams; i++)
			teamOfName.enqueue(teams[i]);
		return teamOfName;
	}
	
	public int wins(String team) {
		int i = 0;
		while (i < numberOfTeams) {
			if (!teams[i].equals(team))
				i++;
			else
				break;
		}
		if (i >= numberOfTeams)
			throw new IllegalArgumentException();
		return w[i];
	}
	
	public int losses(String team) {
		int i = 0;
		while (i < numberOfTeams) {
			if (!teams[i].equals(team))
				i++;
			else
				break;
		}
		if (i >= numberOfTeams)
			throw new IllegalArgumentException();
		return l[i];
	}
	
	public int remaining(String team) {
		int i = 0;
		while (i < numberOfTeams) {
			if (!teams[i].equals(team))
				i++;
			else
				break;
		}
		if (i >= numberOfTeams)
			throw new IllegalArgumentException();
		return r[i];
	}
	
	public int against(String team1, String team2) {
		int i = 0;
		int j = 0;
		while (i < numberOfTeams) {
			if (!teams[i].equals(team1))
				i++;
			else
				break;
		}
		if (i >= numberOfTeams)
			throw new IllegalArgumentException();
		
		while (j < numberOfTeams) {
			if (!teams[j].equals(team2))
				j++;
			else
				break;
		}
		if (j >= numberOfTeams)
			throw new IllegalArgumentException();
		
		return g[i][j];
	}
	
	public boolean isEliminated(String team) {
		int id = 0; // get the id of this team
		while (id < numberOfTeams) {
			if (teams[id].equals(team))
				break;
			id++;
		}
		if (id >= numberOfTeams)
			throw new IllegalArgumentException();
		
		boolean isEliminated = false;
		isEliminatedTeamName = new TreeSet<String>();
		int V = 0;
		int E = 0;
		int base = 0;
		for (int i = 0; i < numberOfTeams - 1; i++)
			base += i;
		V = 1 + base + numberOfTeams - 1 + 1;
		E = base + 2 * base + numberOfTeams - 1;
		FlowEdge[] edge = new FlowEdge[E];
		FlowNetwork network = new FlowNetwork(V);
		/* initialize the edges */
		int count = 0;
		for (int i = 0; i < numberOfTeams; i++) {
			if (i == id)
				continue;
			for (int j = i + 1; j < numberOfTeams; j++) {
				if (j == id)
					continue;
				edge[count] = new FlowEdge(0, count + 1, g[i][j]);
				count++;
			}
		}
		
		for (int i = 0; i < 2 * base; i = i + 2) {
			int sui = i / 2;
			int to = edge[sui].to();
			int id1 = 0;
			int id2 = 0;
			int b = 1;
			for (int m = 0; m < numberOfTeams; m++) {
				if (m == id)
					continue;
				for (int n = m + 1; n < numberOfTeams; n++) {
					if (n == id)
						continue;
					if (to == b) {
						id1 = m;
						id2 = n;
						b++;
						break;
					}
					else
						b++;
				}
			}
			//id1 += base + 1;
			//id2 += base + 1;
			if (edge[sui].capacity() != 0) {
				if (id1 < id)
					edge[count] = new FlowEdge(sui + 1, id1 + base + 1, edge[sui].capacity());
				else
					edge[count] = new FlowEdge(sui + 1, id1 + base, edge[sui].capacity());
			}
			else {
				if (id1 < id)
					edge[count] = new FlowEdge(sui + 1, id1 + base + 1, Integer.MAX_VALUE);
				else
					edge[count] = new FlowEdge(sui + 1, id1 + base, Integer.MAX_VALUE);
			}
			count++;
			if (edge[sui].capacity() != 0) {
				if (id2 < id)
					edge[count] = new FlowEdge(sui + 1, id2 + base + 1, edge[sui].capacity());
				else
					edge[count] = new FlowEdge(sui + 1, id2 + base, edge[sui].capacity());
			}
			else {
				if (id2 < id)
					edge[count] = new FlowEdge(sui + 1, id2 + base + 1, Integer.MAX_VALUE);
				else
					edge[count] = new FlowEdge(sui + 1, id2 + base, Integer.MAX_VALUE);
			}
			count++;
		}
		
		for (int i = 0; i < numberOfTeams; i++) {
			if (i == id)
				continue;
			int t = 0;
			if (i < id)
				t = i;
			else if (i > id)
				t = i - 1;
			if (w[id] + r[id] - w[i] < 0) {
				isEliminatedTeamName.add(teams[i]);
				return true;
			}
			edge[count] = new FlowEdge(t + base + 1, V - 1, w[id] + r[id] - w[i]);
			count++;		
		}
		
		/* initialize the FlowNetwork */
		for (int i = 0; i < E; i++)
			network.addEdge(edge[i]);
		result = new FordFulkerson(network, 0, V - 1);
		for (int i = 0; i < base; i++) {
			if (edge[i].flow() != edge[i].capacity()) {
				int id1 = 0;
				int id2 = 0;
				int b = 1;
				for (int m = 0; m < numberOfTeams; m++) {
					if (m == id)
						continue;
					for (int n = m + 1; n < numberOfTeams; n++) {
						if (n == id)
							continue;
						if (i + 1 == b) {
							id1 = m;
							id2 = n;
							b++;
							break;
						}
						else
							b++;
					}
				}
				
				if (id1 < id && id2 < id && edge[3 * base + id1] != null 
						&& edge[3 * base + id2] != null) {
					if (edge[3 * base + id1].flow() == edge[3 * base + id1].capacity()
							&& edge[3 * base + id2].flow() == edge[3 * base + id2].capacity())
						isEliminated = true;
				}
				else if (id1 < id && id2 > id && edge[3 * base + id1] != null 
						&& edge[3 * base + id2 - 1] != null) {
					if (edge[3 * base + id1].flow() == edge[3 * base + id1].capacity()
							&& edge[3 * base + id2 - 1].flow() == edge[3 * base + id2 - 1].capacity())
						isEliminated = true;
				}
				else if (id1 > id && id2 < id && edge[3 * base + id1 - 1] != null
						&& edge[3 * base + id2] != null) {
					if (edge[3 * base + id1 - 1].flow() == edge[3 * base + id1 - 1].capacity()
							&& edge[3 * base + id2].flow() == edge[3 * base + id2].capacity())
						isEliminated = true;
				}
				else if (id1 > id && id2 > id && edge[3 * base + id1 - 1] != null
						&& edge[3 * base + id2 - 1] != null) {
					if (edge[3 * base + id1 - 1].flow() == edge[3 * base + id1 - 1].capacity()
							&& edge[3 * base + id2 - 1].flow() == edge[3 * base + id2 - 1].capacity())
						isEliminated = true;
				}
			}		
			
		}
		return isEliminated;	
	}
	
	public Iterable<String> certificateOfElimination(String team) {
		int id = 0; // get the id of this team
		while (id < numberOfTeams) {
			if (teams[id].equals(team))
				break;
			id++;
		}
		if (id >= numberOfTeams)
			throw new IllegalArgumentException();
		if (!isEliminated(team))
			return null;
		if (result == null)
			return isEliminatedTeamName;
		int base = 0;
		for (int i = 0; i < numberOfTeams - 1; i++)
			base += i;
		for (int i = 1; i < base + 1; i++) {
			if (result.inCut(i)) {
				int id1 = 0;
				int id2 = 0;
				int b = 1;
				for (int m = 0; m < numberOfTeams; m++) {
					if (m == id)
						continue;
					for (int n = m + 1; n < numberOfTeams; n++) {
						if (n == id)
							continue;
						if (i == b) {
							id1 = m;
							id2 = n;
							b++;
							break;
						}
						else
							b++;
					}
				}
				if (!isEliminatedTeamName.contains(teams[id1]))
					isEliminatedTeamName.add(teams[id1]);
				if (!isEliminatedTeamName.contains(teams[id2]))
					isEliminatedTeamName.add(teams[id2]);
				
			}
			
			
		}
		return isEliminatedTeamName;
		
	}
	
	
	public static void main(String args[]) {
		BaseballElimination result = new BaseballElimination(args[0]);
		System.out.println(result.numberOfTeams);
		for (int i = 0; i < result.numberOfTeams; i++) {
			System.out.print(result.teams[i] + " ");
			System.out.print(result.w[i] + " ");
			System.out.print(result.l[i] + " ");
			System.out.print(result.r[i] + " ");
			for (int j = 0; j < result.numberOfTeams; j++) {
				System.out.print(result.g[i][j] + " ");
			}
			System.out.println();
		}
		System.out.println(result.wins("Atlanta"));
		
		
	}
}
