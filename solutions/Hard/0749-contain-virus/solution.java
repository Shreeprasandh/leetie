// ──────────────────────────────────────────────────
// Problem  : 749. Contain Virus
// Difficulty: Hard
// Tags     : Array, Depth-First Search, Breadth-First Search, Matrix, Simulation
// Link     : https://leetcode.com/problems/contain-virus/
// Runtime  : 19 ms (beats 10%)
// Memory   : 47124000 (beats 10%)
// Language : java
// Copyright: (c) 2026 Shreeprasandh. All rights reserved.
// Synced by: leetie
// ──────────────────────────────────────────────────

class Solution {

    public int containVirus(int[][] infection) {
        VirusController vc = new VirusController(infection);

        vc.init();
        
        return vc.containVirus();
    }

    class VirusController {

        static final int[][] DIR = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        static final int HEALTHY = 0;
        static final int INFECTED = 1;
        static final int QUARANTINED = 2;

        int[][] infection;
        int m;
        int n;
        int ID;
        List<Virus> virusBlocks;
        Map<Integer, Virus> cellToVirusBlock;
        int walls;

        public VirusController(int[][] infection) {
            this.infection = infection;
            m = infection.length;
            n = infection[0].length;
            ID = 100;
            virusBlocks = new ArrayList<>();
            cellToVirusBlock = new HashMap<>();
            walls = 0;
        }

        // initialize virus blocks
        public void init() {
            for (int i = 0; i < m; i++)
                for (int j = 0; j < n; j++) {
                    if (infection[i][j] == 1 && !cellToVirusBlock.containsKey(encode(i, j))){
                        Virus v = new Virus(ID++);
                        dfs(i, j, v);
                        virusBlocks.add(v);
                    }
                }
            // System.out.println("Init:");
            // print();
        }

        public void dfs(int i, int j, Virus v) {
            int hash = encode(i, j);

            if (i < 0 || i >= m || j < 0 || j >= n)
                return;
            if (infection[i][j] == 0 || cellToVirusBlock.containsKey(hash))
                return;

            v.add(hash);
            cellToVirusBlock.put(hash, v);
            for (int[] d : DIR) {
                dfs(i + d[0], j + d[1], v);
            }
        }

        // solution
        public int containVirus() {
            while (!virusBlocks.isEmpty() && cellToVirusBlock.size() < m * n) {
                // 1. build wall
                // System.out.println("------------------");
                Virus mostDangerous = getMostDangerousVirus();
                walls += buildWall(mostDangerous);
                virusBlocks.remove(mostDangerous);
                // System.out.println("Build wall:");
                // print();

                // 2. infect
                for (Virus v : virusBlocks) {
                    infect(v);
                }
                // System.out.println("Infect:");
                // print();

                // merge
                Set<Virus> visited = new HashSet<>();
                List<Virus> blocks = new ArrayList<>();
                for (Virus v : virusBlocks) {
                    if (visited.contains(v))
                        continue;

                    Set<Virus> neighbors = new HashSet<>();
                    getMerges(v, neighbors, visited);
                    for (Virus neighbor : neighbors) {
                        merge(v, neighbor);
                    }
                    blocks.add(v);
                }
                virusBlocks = blocks;

                // System.out.println("Merge:");
                // print();
            }

            return walls;
        }

        public Virus getMostDangerousVirus() {
            Virus result = null;
            int max = Integer.MIN_VALUE;
            for (Virus v : virusBlocks) {
                int infecting = possibleInfection(v);
                if (infecting > max) {
                    max = infecting;
                    result = v;
                }
            }

            return result;
        }

        // return amount of healthy cell around a virus
        public int possibleInfection(Virus virus) {
            Set<Integer> infecting = new HashSet<>();
            for (Integer hash : virus.area) {
                int[] v = decode(hash);
                for (int[] d : DIR) {
                    int i = v[0] + d[0], j = v[1] + d[1];
                    if (isInfected(i, j)) {
                        infecting.add(encode(i, j));
                    }
                }
            }

            return infecting.size();
        }

        public void getMerges(Virus v, Set<Virus> neighbors, Set<Virus> visited) {
            if (visited.contains(v))
                return;

            visited.add(v);
            neighbors.add(v);
            for (Virus neighbor : neighbors(v)) {
                getMerges(neighbor, neighbors, visited);
            }
        }

        // get all neighbors of a virus block
        public Set<Virus> neighbors(Virus virus) {
            Set<Virus> neighbors = new HashSet<>();

            for (Integer hash : virus.area) {
                int[] v = decode(hash);
                for (int[] d : DIR) {
                    int i = v[0] + d[0], j = v[1] + d[1];
                    if (i < 0 || i >= m || j < 0 || j >= n)
                        continue;
                    if (infection[i][j] == INFECTED) {
                        Virus neighbor = cellToVirusBlock.get(encode(i, j));
                        if (neighbor != virus) {
                            neighbors.add(neighbor);
                        }
                    }
                }
            }

            return neighbors;
        }


        // build walls around a virus, return amount of built walls
        public int buildWall(Virus virus) {
            int result = 0;
            for (Integer hash : virus.area) {
                int[] v = decode(hash);
                for (int[] d : DIR) {
                    int i = v[0] + d[0], j = v[1] + d[1];
                    if (isInfected(i, j)) {
                        result++;
                    }
                }
                infection[v[0]][v[1]] = QUARANTINED;
            }

            return result;
        }

        // infect virus
        public void infect(Virus virus) {
            Set<Integer> infected = new HashSet<>();
            for (Integer hash : virus.area) {
                int[] v = decode(hash);
                for (int[] d : DIR) {
                    int i = v[0] + d[0], j = v[1] + d[1];
                    int encode = encode(i, j);
                    if (isInfected(i, j)) {
                        infection[i][j] = INFECTED;
                        infected.add(encode);
                        cellToVirusBlock.put(encode, virus);
                    }
                }
            }
            virus.area.addAll(infected);
        }

        // merge v2 into v1
        public void merge(Virus v1, Virus v2) {
            for (Integer cell : v2.area) {
                v1.area.add(cell);
                cellToVirusBlock.put(cell, v1);
            }
        }

        // check whether a cell is infectable
        private boolean isInfected(int i, int j) {
            if (i < 0 || i >= m || j < 0 || j >= n)
                return false;
            return infection[i][j] == HEALTHY;
        }

        // helper functions mapping coordinate to a distinct int mutually
        private int[] decode(int hash) {
            int j = hash % n;
            int i = hash / n;

            return new int[] {i, j};
        }

        private int encode(int i, int j) {
            return i * n + j;
        }

        // for test
        // public void print() {
        //     System.out.println("[");
        //     for (int i = 0; i < m; i++) {
        //         System.out.print("[");
        //         int j = 0;
        //         for (; j < n - 1; j++) {
        //             if (infection[i][j] == 0 || infection[i][j] == 2)
        //                 System.out.print(infection[i][j] + ",");
        //             else
        //                 System.out.print(cellToVirusBlock.get(i * n + j).id + ",");
        //         }
        //         if (infection[i][j] == 0 || infection[i][j] == 2)
        //                 System.out.print(infection[i][j]);
        //         else
        //             System.out.print(cellToVirusBlock.get(i * n + j).id);
        //         System.out.println("],");
        //     }
        //     System.out.println("]");
        // }
    }

    class Virus {
        
        Set<Integer> area;
        int id;

        public Virus(int id) {
            area = new HashSet<>();
            this.id = id;
        }

        // Add a cell to area
        public void add(int hash) {
            area.add(hash);
        }

    }
}