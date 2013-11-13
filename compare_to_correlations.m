function compare(correlations)

load data_from_anael/graphs.mat;

ids = get(graph_correlation.Edges,'ID');
anael = [];
for row = 1:length(ids)
    text = deal(ids{row});
    anael(row) = str2idx(text);
end


idx = find(~isnan(correlations));
values = correlations(idx);

ranks = sortrows([idx, values], [-2]);

georgina = ranks(1:48,1);

both = intersect(anael,georgina);
for pair = both
    [i,j] = ind2sub([16 16],pair);
    disp([num2str(i),' -> ', num2str(j)]);
end
size(both)

