load results/frechet_distances_all_overlap.mat;
load georgina_correlations33.mat;
N = size(results,1);
copy_f = [];
copy_c = [];
for i = 1:N
    for j = i:N
        copy_f(i,j) = results(i,j);
        copy_f(j,i) = results(i,j);
        copy_c(i,j) = correlation_filter(i,j);
        copy_c(j,i) = correlation_filter(i,j);
    end
end
count = 0;
for i = 1:N
    closest = min(copy_f(:,i));
    correlated = max(copy_c(:,i));
    j = find(copy_f(:,i) == closest);
    j2 = find(copy_c(:,i) == correlated);
    fprintf('%d, %d: %f   %d: %f\n',i,j,closest,j2,correlated);
    if j ~= j2
        count = count + 1;
    end
end
count
