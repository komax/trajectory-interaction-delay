function t = interpolate(bird,bird_id,method)

X = bird(:,4);
Vx = bird(:,1);
Vy = bird(:,2);
Vz = bird(:,3);

start_frame = min(X);
stop_frame = max(X);

Xq = start_frame:stop_frame;

Vqx = interp1(X,Vx,Xq, method);
Vqy = interp1(X,Vy,Xq, method);
Vqz = interp1(X,Vz,Xq, method);

t = [Vqx' Vqy' Vqz' Xq' repmat(bird_id,length(Xq),1)];
