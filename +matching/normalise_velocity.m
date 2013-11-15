function dV = normalise_velocity(dVin)
%%% function dV = normalise_velocity(dVin)
%%% Convert all velocity measures to vectors of length 1
    dV = dVin ./ repmat(sqrt(sum(dVin .* dVin,2)), 1, 3);
    if any(isnan(dV))
        for row = 1:size(dV,1)
            if any(isnan(dV(row,:)))
                fprintf(2,'error, in: %f %f %f, out: %f %f %f\n',dVin(row,:),dV(row,:));
            end

    end
end
