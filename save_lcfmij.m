function blah(i,j,fname)

m = filtered_lcfm(i,j);
matching = [m.i m.j]
save(['precomputed/',fname, '.mat'], 'matching')
