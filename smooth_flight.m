function trajOUT = smooth_flight(trajIN, Wn)
coords = trajIN(:,1:3)


[b, a] = butter(2,Wn);
Hd = dfilt.df1(b,a);

%%%offset = bird1(1,1:3);
velocities = bird1(:,1:3) - [0 0 0; bird1(:,1:3)]



