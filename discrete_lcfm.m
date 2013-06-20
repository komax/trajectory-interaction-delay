function matching = compute_discrete_locally_correct_frechet(t1, t2)
%%%function matching = compute_discrete_locally_correct_frechet(t1, t2)
%%% given two trajectories t1 and t2, parameterised by t1(:,4) and
%%% t2(:,4) respectively (frame IDs), return an array of grid points
%%% [ n1 m1; n2 m2; n3 m3; ... ] that represents a matching between the 
%%% two trajectories. E.g. frameID n1 of t1 should be matched with frameID 
%%% m1 of t2.
javaaddpath .
import frechet.*;

%%%grid = compute_distance_terrain(t1, t2);
grid = zeros(length(t1),length(t2));
for i = 1:length(t1)
    for j = 1:length(t2)
        p = t1(i,1:3);
        q = t2(j,1:3);
        d = norm(p - q,2);
        grid(i,j) = d;
    end
end
rows = length(t1);
cols = length(t2);

matching = LocallyCorrectFrechet.compute(grid, t1, t2, rows, cols);
