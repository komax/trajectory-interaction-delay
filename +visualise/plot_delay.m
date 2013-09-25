function plot_delay(t1, t2, matching_obj)
    results = [matching_obj.i matching_obj.j];
    y = [];
    for row = 1:size(results,1)
%%%        results(row,:)
        i = results(row,1) + 1;
        j = results(row,2) + 1;
        frame1 = t1(i,4);
        frame2 = t2(j,4);
        y(row) = frame2 - frame1;
    end
    plot(y ./35) % convert from 35 frames per second
    ylabel('seconds')
    

end

