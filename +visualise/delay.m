function  delay()
for i = 1:16
    figure
    hold on;
    for j = 1:16
        if i ~= j
            [x, y] = closest_point(get_bird(i), get_bird(j));
            plot(x,y,'.');
        end
    end
    disp([num2str(i),' ', num2str(j)]);
    input('next?')
end

end
