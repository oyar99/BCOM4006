class GCN:
    '''
    GCN - Gene Co-expression Network with utility methods for
    statistical analysis.
    '''
    adj = {}

    def add_edge(u, v):
        self.adj[u].add(v)
        self.adj[v].add(u)

    def degrees():
        degrees = {}
        
        for u in self.adj:
            degrees[u] = len(self.adj[u])

        return degrees

    def degree_distribution():
        distr = {}

        degrees = self.degrees()

        for u in degrees:
            if degrees[u] in distr:
                distr[degrees[u]] = distr[degrees[u]] + 1
            else:
                distr[degrees[u]] = 1

        return distr

    def density():

    def clustering_coefficient():
        


    