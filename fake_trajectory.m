function t = fake_trajectory(start_x, start_y, start_z, length, turniness)
frameIDs = 1:length;
coords = [];
direction = [rand * 2 * pi, rand * 2 * pi];
speed = rand;
x = start_x;
y = start_y;
z = start_z;
for id = frameIDs
    if mod(id, turniness) == 0
        direction = [rand * 2 * pi, rand * 2 * pi];
    end
    coords(1,id) = x;
    coords(2,id) = y;
    coords(3,id) = z;
    speed = rand;
    x = x + speed * sin(direction(1)) * cos(direction(2));
    y = y + speed * sin(direction(1)) * sin(direction(2));
    z = z + speed * cos(direction(1));

    
end
size(coords)
size(frameIDs)
t = [coords' frameIDs'];



