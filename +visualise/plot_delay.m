function plot_delay(t1, t2, matching_obj)
    results = [matching_obj.i matching_obj.j];
    y = [];
    x = [];
    for row = 1:size(results,1)
%%%        results(row,:)
        i = results(row,1) + 1;
        j = results(row,2) + 1;
        frame1 = t1(i,4);
        frame2 = t2(j,4);
        y(row) = frame2 - frame1;
        x(row) = mean([frame1 frame2]);
    end
    plot(x,y ./35) % convert from 35 frames per second
    ylabel('seconds');
    xlabel('average frame ID');
    

end

