function plot_distance(t1, t2, matching_java_obj)

results = [matching_java_obj.i matching_java_obj.j];
hold on;
y = [];
for row = 1:size(results,1)
    i = results(row,1) + 1; % because java array starts from 0
    j = results(row,2) + 1; %
    p = t1(i,1:3);
    q = t2(j,1:3);
    d = norm(p - q, 2);
    if size(t1,2) >= 4 && size(t2,2) >= 4 %  if we have timing data
        delta_t = t1(i,4) - t2(j,4);
        if delta_t == 0
            fprintf(2, 'Matched pairs are from the same frame.\n');
            scatter(row, d, 'k', 'filled')
        elseif delta_t > 0
            scatter(row, d, 'b', 'filled')
        else
            scatter(row, d, 'r', 'filled')
        end

%%%        d  = d * sign(delta_t);

    y(row) = d;
end
%%%plot(y)

end
