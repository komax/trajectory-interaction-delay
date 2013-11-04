function g = build_correlation_graph(correlation, delay, cutoff)
N = size(correlation,1);
cm = sparse(N,N);

for i = 1:N
    for j = i+1:N
        if correlation(i,j) >= cutoff
            if delay(i,j) >= 0
                cm(i,j) = correlation(i,j);
            else
                cm(j,i) = correlation(i,j);
            end
        end
    end
end
g = biograph(cm);
if graphisdag(cm)
    fprintf(1,'Graph has no cycles\n');
else
    fprintf(1, 'Graph has at least one cycle\n');
end

end
