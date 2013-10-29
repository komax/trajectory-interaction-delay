t1 = [0 0 0 1; 0 1 0 2; 1 1 0 3; 1 2 0 4; 2 2 0 5;];
[v1,frames] = smoothing.flight_velocities(t1);
[v1 frames]

t2 = [0 0 0 1; 0 3 0 3; 0 4 0 6];
[v2,frames] = smoothing.flight_velocities(t2);
[v2 frames]

t3 = [1 1 0 5; 1 1 0 6; 4 5 0 7;];
[v3, frames] = smoothing.flight_velocities(t3);
[v3 frames]
