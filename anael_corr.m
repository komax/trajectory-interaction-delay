function [correl, delay] = anael_corr(b1, b2)
[x1, y1, z1, id1] = flight_direction(b1);
[x2, y2, z2, id2] = flight_direction(b2);

directions1 = [x1 y1 z1 id1];
directions2 = [x2 y2 z2 id2];

max_delay = max([id1; id2]) - min([id1; id2])

delay = 0:max_delay;
correl(:,1) = delay;
 


end
