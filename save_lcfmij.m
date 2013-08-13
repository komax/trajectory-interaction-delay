function blah(i,j,fname)
bird_i = get_bird(i);
bird_j = get_bird(j);

m = discrete_lcfm(bird_i,bird_j);
matching = [m.i m.j]
save(['precomputed/',fname, '.mat'], 'matching')
